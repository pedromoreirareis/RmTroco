package com.pedromoreirareisgmail.rmtroco;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    private EditText mEtValorVenda;
    private EditText mEtValorRecebido;
    private Button mButCalcular;
    private Button mButLimpar;
    private TextView mTvTroco;

    private boolean isFormatarCurrencyAtualizado = false;
    private static final NumberFormat formatarNumero = NumberFormat.getCurrencyInstance();

    private final EditText.OnTouchListener mTouchListnerEditFocoCursorFim = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            int id = view.getId();

            switch (id) {

                case R.id.et_valor_recebido:
                    mEtValorRecebido.requestFocus();
                    mEtValorRecebido.setSelection(mEtValorRecebido.getText().length());
                    return true;

                case R.id.et_valor_venda:
                    mEtValorVenda.requestFocus();
                    mEtValorVenda.setSelection(mEtValorVenda.getText().length());
                    return true;

                default:
                    return false;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mEtValorVenda = (EditText) findViewById(R.id.et_valor_venda);
        mEtValorRecebido = (EditText) findViewById(R.id.et_valor_recebido);
        mButCalcular = (Button) findViewById(R.id.but_calcular);
        mButLimpar = (Button) findViewById(R.id.but_limpar);
        mTvTroco = (TextView) findViewById(R.id.tv_troco);

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
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        mButLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mEtValorRecebido.setText("0");
                mEtValorVenda.setText("0");
                mTvTroco.setText("");

                mEtValorVenda.requestFocus();
            }
        });

        mButCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                double valorVenda = formatarParaDouble(mEtValorVenda.getText().toString().trim());
                double valorRecebido = formatarParaDouble(mEtValorRecebido.getText().toString().trim());

                if(valorVenda == 0){
                    mEtValorVenda.setError(getString(R.string.valor_error));
                    mEtValorVenda.requestFocus();
                    mTvTroco.setText("");
                }

                if(valorRecebido == 0){
                    mEtValorRecebido.setError(getString(R.string.valor_error));
                    mEtValorRecebido.requestFocus();
                    mTvTroco.setText("");
                }

                if(valorVenda > valorRecebido){

                    mEtValorRecebido.setError(getString(R.string.valor_maior ) + " " + formatarDoubleParaCurrency(valorVenda));
                    mTvTroco.setText("");

                }else{

                    mTvTroco.setText(formatarDoubleParaCurrency(valorRecebido - valorVenda));
                }
            }
        });

        mEtValorRecebido.setOnTouchListener(mTouchListnerEditFocoCursorFim);
        mEtValorVenda.setOnTouchListener(mTouchListnerEditFocoCursorFim);

        semCursorFocoSelecaoZerado(mEtValorRecebido);
        semCursorFocoSelecaoZerado(mEtValorVenda);

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
