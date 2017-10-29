package com.pedromoreirareisgmail.rmtroco;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    private static final NumberFormat formatarNumero = NumberFormat.getCurrencyInstance();
    private double mValorVenda = 0;
    private double mValorRecebido = 0;
    private String mTextoErro = "";
    private EditText mEtValorVenda;
    private EditText mEtValorRecebido;
    private final EditText.OnTouchListener mTouchListnerEditFocoCursorFim = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            int id = view.getId();

            switch (id) {

                case R.id.et_valor_recebido:
                    mEtValorRecebido.requestFocus();
                    mEtValorRecebido.setSelection(mEtValorRecebido.getText().length());
                    mostrarTeclado(mEtValorRecebido);
                    return true;

                case R.id.et_valor_venda:
                    mEtValorVenda.requestFocus();
                    mEtValorVenda.setSelection(mEtValorVenda.getText().length());
                    mostrarTeclado(mEtValorVenda);
                    return true;

                default:
                    return false;
            }
        }
    };
    private TextView mTvTroco;
    private TextView mTvTrocoLabel;
    private boolean isFormatarCurrencyAtualizado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mEtValorVenda = (EditText) findViewById(R.id.et_valor_venda);
        mEtValorRecebido = (EditText) findViewById(R.id.et_valor_recebido);
        Button mButLimpar = (Button) findViewById(R.id.but_limpar);
        mTvTroco = (TextView) findViewById(R.id.tv_troco);
        mTvTrocoLabel = (TextView) findViewById(R.id.tv_troco_label);

        mEtValorRecebido.setOnTouchListener(mTouchListnerEditFocoCursorFim);
        mEtValorVenda.setOnTouchListener(mTouchListnerEditFocoCursorFim);


        mEtValorRecebido.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                if (isFormatarCurrencyAtualizado) {
                    isFormatarCurrencyAtualizado = false;
                    return;
                }

                isFormatarCurrencyAtualizado = true;

                mEtValorRecebido.setText(formatarParaCurrency(charSequence.toString().trim()));
                mEtValorRecebido.setSelection(mEtValorRecebido.getText().length());

                calcularTroco();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mEtValorVenda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                if (isFormatarCurrencyAtualizado) {
                    isFormatarCurrencyAtualizado = false;
                    return;
                }

                isFormatarCurrencyAtualizado = true;

                mEtValorVenda.setText(formatarParaCurrency(charSequence.toString().trim()));
                mEtValorVenda.setSelection(mEtValorVenda.getText().length());

                calcularTroco();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mTvTroco.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count > 11) {
                    mTvTroco.setTextSize(44);
                } else if (count > 10) {
                    mTvTroco.setTextSize(48);
                } else if (count > 9) {
                    mTvTroco.setTextSize(52);
                } else if (count > 8) {
                    mTvTroco.setTextSize(56);
                } else if (count > 7) {
                    mTvTroco.setTextSize(64);
                } else {
                    mTvTroco.setTextSize(76);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        semCursorFocoSelecaoZerado(mEtValorRecebido);
        semCursorFocoSelecaoZerado(mEtValorVenda);

        mButLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mValorVenda = 0;
                mValorRecebido = 0;
                mTextoErro = "";

                mEtValorRecebido.setError(null);
                mEtValorVenda.setText("0");
                mEtValorRecebido.setText("0");
                mTvTroco.setText("");

                mEtValorVenda.requestFocus();
            }
        });


    }


    private void calcularTroco() {

        mValorVenda = formatarParaDouble(mEtValorVenda.getText().toString().trim());
        mValorRecebido = formatarParaDouble(mEtValorRecebido.getText().toString().trim());
        mTextoErro = getString(R.string.valor_maior) + " " + formatarDoubleParaCurrency(mValorVenda);

        if (mValorVenda != 0 && mValorRecebido != 0) {

            if (mValorVenda > mValorRecebido) {

                mEtValorRecebido.setError(mTextoErro);
                mTvTrocoLabel.setText("");
                mTvTroco.setText("");

            } else {

                mTvTroco.setText(formatarDoubleParaCurrency(mValorRecebido - mValorVenda));
                mTvTrocoLabel.setText(getString(R.string.valor_troco));
                mEtValorRecebido.setError(null);
            }
        }
    }


    /*

    Utils

     */

    private void mostrarTeclado(EditText meuEdit) {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(meuEdit, InputMethodManager.SHOW_IMPLICIT);
    }

    private String formatarParaCurrency(String str) {

        boolean hasMask =
                (((str.contains("R$")) || (str.contains("$"))) &&
                        ((str.contains(".")) || (str.contains(","))));

        if (hasMask) {

            str = str.replaceAll("[^\\d]", "");
        }

        try {

            return formatarNumero.format(Double.parseDouble(str) / 100);

        } catch (NumberFormatException e) {

            return "";
        }
    }


    private String formatarDoubleParaCurrency(double valorParaFormatar) {

        NumberFormat formatarCurrency = NumberFormat.getCurrencyInstance();

        return formatarCurrency.format(valorParaFormatar);
    }

    private double formatarParaDouble(String str) {

        boolean hasMask =
                (((str.contains("R$")) || (str.contains("$"))) &&
                        ((str.contains(".")) || (str.contains(","))));

        if (hasMask) {
            str = str.replaceAll("[^\\d]", "");
        }

        try {

            return (Double.parseDouble(str) / 100);

        } catch (NumberFormatException e) {

            return 0;
        }
    }

    private void semCursorFocoSelecaoZerado(EditText editText) {

        editText.setText("0");
        //  editText.setCursorVisible(false);
        editText.setSelectAllOnFocus(false);
    }
}
