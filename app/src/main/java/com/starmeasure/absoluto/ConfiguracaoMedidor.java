package com.starmeasure.absoluto;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.starmeasure.absoluto.fasores.DiagramaFasorialView;
import com.starmeasure.absoluto.fasores.DiagramaFasorialView.Fasor;
import com.starmeasure.protocoloabsoluto.ComandoAbsoluto;
import com.starmeasure.protocoloabsoluto.MedidorAbsoluto;
import com.starmeasure.protocoloabsoluto.ProtocoloAbsoluto;
import com.starmeasure.protocoloabsoluto.RespostaAbsoluto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class ConfiguracaoMedidor extends BaseBluetoothActivity{

    private static Button btSM1;
    private static Button btSM2;
    private static Button btSM3;
    private static Button btSM4;
    private static Button btSM5;
    private static Button btSM6;
    private static Button btSM7;
    private static Button btSM8;
    private static Button btSM9;
    private static Button btSM10;
    private static Button btSM11;
    private static Button btSM12;
    private static Button btSB1;
    private static Button btSB2;
    private static Button btSB3;
    private static Button btSB4;
    private static Button btSB5;
    private static Button btSB6;
    private static Button btSB7;
    private static Button btSB8;
    private static Button btSB9;
    private static Button btSB10;
    private static Button btSB11;
    private static Button btST1;
    private static Button btST2;
    private static Button btST3;
    private static Button btST4;
    private static Button btST5;
    private static Button btST6;
    private static Button btST7;
    private static Button btST8;
    private static Button btST9;
    private static Button btST10;

    private static Button btSH1;

    private static EditText etUC;
    private static EditText etIU;
    private static EditText etNumeroMedidor;
    private static EditText etDI;

    private static ArrayList<MedidorAbsoluto> medidores;

    long mMedidores1;
    long mMedidores2;
    long mMedidores3;
    long mMedidores4;
    long mMedidores5;
    long mMedidores6;
    long mMedidores7;
    long mMedidores8;

    String mMedidoresCom1;
    String mMedidoresCom2;
    String mMedidoresCom3;
    String mMedidoresCom4;
    String mMedidoresCom5;
    String mMedidoresCom6;
    String mMedidoresCom7;
    String mMedidoresCom8;
    String mMedidoresCom9;
    String mMedidoresCom10;
    String mMedidoresCom11;
    String mMedidoresCom12;

    int QtdMonofasicosDisponiveis = 0;
    int QtdBifasicosDisponiveis = 0;
    int QtdTrifasicosDisponiveis = 0;
    int QtdHexafasicosDisponiveis = 0;

    int MedidoresMonofasicosDisponiveis = 0xFFFF;     // 12 saídas indisponíveis (SM0... SM12)
    int MedidoresBifasicosDisponiveis = 0x07FF;  // 11 saídas disponíveis (SB1... SB11)
    int MedidoresTrifasicosDisponiveis = 0x03FF; // 10 saídas disponíveis (ST1... ST10)
    int MedidoresHexafasicosDisponiveis = 0x0001;// 1 saída disponível (SH1)

    byte IndexMedidorAtivar = 0;
    Spinner mTipoLigacao;

    private static String sTipoLigacao = "";
    private static String SenhaDigitada = "";
    private long tempoEnvio = 0;

    private static final String TAG = ConfiguracaoMedidor.class.getSimpleName();
    public static final int TEMPO_LEITURA_PERIODICA = 1000;
    private DeviceActivity.TipoMedidor tipo_medidor = DeviceActivity.TipoMedidor.ABSOLUTO;


    private String mNumeroMedidor;
    private String mUnidadeConsumidora;
    private String mNumeroMedidorArquivo;
    private String mDeviceName;
    private String mDataMedidor;
    private String mVersaoMedidor;
    private String mEstadoTampaMedidor;
    private byte mMetodoNumeroSerial;
    private byte[] CodMostradores = new byte[9];
    private int mByteCount = 0;

    private boolean isEasyTrafo = false;
    private boolean brespostaAB03 = false;
    private boolean brespostaAB04 = false;
    private boolean bSenhaDigitada = false;

    private ByteArrayOutputStream mByteArrayData = new ByteArrayOutputStream();
    RespostaAbsoluto.GrandezasInstantaneas g;

    private ImageButton mAtualizar;
    private ImageButton mSalvar;
    private TextView mHorarioMedidor;
    private TextView mTempoPacote;
    private TextView mHorarioLocal;
    private TextView mTemperatura;
    private TextView mVersao;
    private TextView mEstadoTampa;
    private TextView mVa;
    private TextView mVb;
    private TextView mVc;
    private TextView mVab;
    private TextView mVbc;
    private TextView mVca;
    private TextView mAnguloVa;
    private TextView mAnguloVb;
    private TextView mAnguloVc;
    private TextView mIa;
    private TextView mIb;
    private TextView mIc;
    private TextView mDefasagemIa;
    private TextView mDefasagemIb;
    private TextView mDefasagemIc;
    private TextView mFrequencia;
    private TextView mPa;
    private TextView mPb;
    private TextView mPc;
    private TextView mPt;
    private TextView mQa;
    private TextView mQb;
    private TextView mQc;
    private TextView mQt;
    private TextView mSa;
    private TextView mSb;
    private TextView mSc;
    private TextView mSt;
    private TextView mFPa;
    private TextView mFPb;
    private TextView mFPc;
    private TextView mFPt;
    private TextView mCFa;
    private TextView mCFb;
    private TextView mCFc;
    private TextView mCFt;
    private TextView mDhtA;
    private TextView mDhtB;
    private TextView mDhtC;
    private TextView mDhcA;
    private TextView mDhcB;
    private TextView mDhcC;

    private DiagramaFasorialView mDiagramaFasorial;

    private TimerTask mAtualizarTask;
    private Timer mAtualizarTimer = new Timer();

    public enum TipoOperacao {
        Ativacao,
        Desativacao,
        CodMostradores,
        FIM
    }

    private TipoOperacao funcaoEmExecucao = TipoOperacao.FIM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuracao);

        btSM1 = findViewById(R.id.btnS1);
        btSM2 = findViewById(R.id.btnS2);
        btSM3 = findViewById(R.id.btnS3);
        btSM4 = findViewById(R.id.btnS4);
        btSM5 = findViewById(R.id.btnS5);
        btSM6 = findViewById(R.id.btnS6);
        btSM7 = findViewById(R.id.btnS7);
        btSM8 = findViewById(R.id.btnS8);
        btSM9 = findViewById(R.id.btnS9);
        btSM10 = findViewById(R.id.btnS10);
        btSM11 = findViewById(R.id.btnS11);
        btSM12 = findViewById(R.id.btnS12);

        btSB1 = findViewById(R.id.btnS0102);
        btSB2 = findViewById(R.id.btnS0203);
        btSB3 = findViewById(R.id.btnS0304);
        btSB4 = findViewById(R.id.btnS0405);
        btSB5 = findViewById(R.id.btnS0506);
        btSB6 = findViewById(R.id.btnS0607);
        btSB7 = findViewById(R.id.btnS0708);
        btSB8 = findViewById(R.id.btnS0809);
        btSB9 = findViewById(R.id.btnS0910);
        btSB10 = findViewById(R.id.btnS1011);
        btSB11 = findViewById(R.id.btnS1112);

        btST1 = findViewById(R.id.btnS010203);
        btST2 = findViewById(R.id.btnS020304);
        btST3 = findViewById(R.id.btnS030405);
        btST4 = findViewById(R.id.btnS040506);
        btST5 = findViewById(R.id.btnS050607);
        btST6 = findViewById(R.id.btnS060708);
        btST7 = findViewById(R.id.btnS070809);
        btST8 = findViewById(R.id.btnS080910);
        btST9 = findViewById(R.id.btnS091011);
        btST10 = findViewById(R.id.btnS101112);

        btSH1 = findViewById(R.id.btnSH1);

        etUC = findViewById(R.id.etNumeroUC);
        etIU = findViewById(R.id.etNumeroIU);
        etNumeroMedidor = findViewById(R.id.etNumeroMedidor);

        mTipoLigacao = findViewById(R.id.spinner_tipo_ligacao);

        final Intent intent = getIntent();
        mMedidores1 = intent.getLongExtra(Consts.EXTRAS_MEDIDORES1, mMedidores1);
        mMedidores2 = intent.getLongExtra(Consts.EXTRAS_MEDIDORES2, mMedidores2);
        mMedidores3 = intent.getLongExtra(Consts.EXTRAS_MEDIDORES3, mMedidores3);
        mMedidores4 = intent.getLongExtra(Consts.EXTRAS_MEDIDORES4, mMedidores4);
        mMedidores5 = intent.getLongExtra(Consts.EXTRAS_MEDIDORES5, mMedidores5);
        mMedidores6 = intent.getLongExtra(Consts.EXTRAS_MEDIDORES6, mMedidores6);
        mMedidores7 = intent.getLongExtra(Consts.EXTRAS_MEDIDORES7, mMedidores7);
        mMedidores8 = intent.getLongExtra(Consts.EXTRAS_MEDIDORES8, mMedidores8);
        medidores = (ArrayList<MedidorAbsoluto>) getIntent().getExtras().getSerializable(Consts.EXTRAS_MEDIDORES);
        mMedidoresCom1 = intent.getStringExtra(Consts.EXTRAS_MEDIDORESCOM1);
        mMedidoresCom2 = intent.getStringExtra(Consts.EXTRAS_MEDIDORESCOM2);
        mMedidoresCom3 = intent.getStringExtra(Consts.EXTRAS_MEDIDORESCOM3);
        mMedidoresCom4 = intent.getStringExtra(Consts.EXTRAS_MEDIDORESCOM4);
        mMedidoresCom5 = intent.getStringExtra(Consts.EXTRAS_MEDIDORESCOM5);
        mMedidoresCom6 = intent.getStringExtra(Consts.EXTRAS_MEDIDORESCOM6);
        mMedidoresCom7 = intent.getStringExtra(Consts.EXTRAS_MEDIDORESCOM7);
        mMedidoresCom8 = intent.getStringExtra(Consts.EXTRAS_MEDIDORESCOM8);
        mMedidoresCom9 = intent.getStringExtra(Consts.EXTRAS_MEDIDORESCOM9);
        mMedidoresCom10 = intent.getStringExtra(Consts.EXTRAS_MEDIDORESCOM10);
        mMedidoresCom11 = intent.getStringExtra(Consts.EXTRAS_MEDIDORESCOM11);
        mMedidoresCom12 = intent.getStringExtra(Consts.EXTRAS_MEDIDORESCOM12);
        mMetodoNumeroSerial = intent.getByteExtra(Consts.EXTRAS_METODO_NUMERO_SERIAL, mMetodoNumeroSerial);

        long mMedidoresAtivos = 0;

        mMedidoresAtivos = mMedidores8 << 56;
        mMedidoresAtivos |= mMedidores7 << 48;
        mMedidoresAtivos |= mMedidores6 << 40;
        mMedidoresAtivos |= mMedidores5 << 32;
        mMedidoresAtivos |= mMedidores4 << 24;
        mMedidoresAtivos |= mMedidores3 << 16;
        mMedidoresAtivos |= mMedidores2 << 8;
        mMedidoresAtivos |= mMedidores1;

        PreparaTela(mMedidoresAtivos);

        findViewById(R.id.voltar_tela).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.tipo_ligacao_medidor));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTipoLigacao.setAdapter(adapter);


        mTipoLigacao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sTipoLigacao = parent.getItemAtPosition(position).toString();
                if(sTipoLigacao.trim().equals("Monofásico")){
                   Toast.makeText(getApplicationContext(), "Selecionado medidor monofásico.", Toast.LENGTH_SHORT).show();
                }
                else if(sTipoLigacao.trim().equals("Bifásico")){
                    Toast.makeText(getApplicationContext(), "Selecionado medidor bifásico.", Toast.LENGTH_SHORT).show();
                }
                else if(sTipoLigacao.trim().equals("Trifásico")){
                    Toast.makeText(getApplicationContext(), "Selecionado medidor trifásico.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //**************************** Botão S1 ********************************
        findViewById(R.id.btnS1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UCAux = "";
                ColorStateList CorLidaBotao;

                CorLidaBotao = v.getBackgroundTintList();
                if(CorLidaBotao == ColorStateList.valueOf(Color.RED)){
                    // Botão SM1 com cor vermelha

                    String tipoMedidor = "";
                    String NumMedidorAux = "";
                    int iUC = 0;
                    for(int z=0;z < medidores.size(); z++){
                        int sTipo = medidores.get(z).tipo;
                        if(sTipo == 1){
                            UCAux = medidores.get(z).unidadeConsumidora;
                            if(UCAux.length() > 8){
                                UCAux = UCAux.substring(UCAux.length()-8, UCAux.length());
                            }
                            etUC.setText(UCAux);
                            NumMedidorAux = medidores.get(z).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Desativacao;

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja desativar o medidor SM1 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                            break;
                        }
                    }
                }
                else if(CorLidaBotao == ColorStateList.valueOf(Color.parseColor("#FF669900"))){
                    // Botão SM1 com cor verde

                    // Testar se está selecionado monofásico
                    if(sTipoLigacao.trim().equals("Nenhum".trim())){
                        Toast.makeText(getApplicationContext(), "Selecione o tipo de ligação antes de ativar.", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                        funcaoEmExecucao = TipoOperacao.FIM;
                        return;
                    }
                    else if(sTipoLigacao.trim().equals("Monofásico".trim())){
                        if((etUC.getText().toString().trim().equals("".trim()))||(etIU.getText().toString().trim().equals("".trim()))){
                            Toast.makeText(getApplicationContext(), "Preencha os campos UC, IU antes da ativação", Toast.LENGTH_SHORT).show();
                            funcaoEmExecucao = TipoOperacao.FIM;
                            return;
                        }

                        String tipoMedidor = "";
                        String NumMedidorAux = "";
                        int iUC = 0;

                        UCAux = etUC.getText().toString();
                        NumMedidorAux = medidores.get(0).numero;
                        etNumeroMedidor.setText(NumMedidorAux);
                        iUC = Integer.parseInt(UCAux);
                        funcaoEmExecucao = TipoOperacao.Ativacao;
                        IndexMedidorAtivar = 1;

                        AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                android.R.style.Theme_Material_Dialog_Alert);
                        String finalNumMedidorAux = NumMedidorAux;
                        AlertDialog alertDialog = builder
                                .setTitle("Ativação / Desativação")
                                .setMessage("Você deseja ativar o medidor monofásico SM1 ?")
                                //.setView(alertText)
                                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                    }
                                })
                                .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                    }
                                }).create();
                        alertDialog.getWindow().setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        alertDialog.show();
                    }
                    else if(sTipoLigacao.trim().equals("Bifásico".trim())){
                        if((etUC.getText().toString().trim().equals("".trim()))||(etIU.getText().toString().trim().equals("".trim()))){
                            Toast.makeText(getApplicationContext(), "Preencha os campos UC, IU antes da ativação", Toast.LENGTH_SHORT).show();
                            funcaoEmExecucao = TipoOperacao.FIM;
                            return;
                        }
                        funcaoEmExecucao = TipoOperacao.FIM;
                        if(btSM2.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900"))){
                            // Botão SM1 e SM2 livres, ativar SB1
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 13; // SB1

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor bifásico SB1 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        return;
                    }
                    else if(sTipoLigacao.trim().equals("Trifásico".trim())){
                        //Toast.makeText(getApplicationContext(), "Ativação Trifásica não implementada.", Toast.LENGTH_SHORT).show();
                        if((etUC.getText().toString().trim().equals("".trim()))||(etIU.getText().toString().trim().equals("".trim()))){
                            Toast.makeText(getApplicationContext(), "Preencha os campos UC, IU antes da ativação", Toast.LENGTH_SHORT).show();
                            funcaoEmExecucao = TipoOperacao.FIM;
                            return;
                        }
                        funcaoEmExecucao = TipoOperacao.FIM;
                        if((btSM2.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900"))) &&
                        (btSM3.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900"))))
                        {
                            // Botão SM1, SM2 e SM3 livres
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 24; // ST1

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor trifásico ST1 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        return;
                    }
                }
                //Toast.makeText(getApplicationContext(), "Botão SM1", Toast.LENGTH_SHORT).show();
            }
        });
        //************************ FIM Botão S1 ********************************

        //*********************** INICIO Botão S2 ******************************
        findViewById(R.id.btnS2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UCAux = "";
                ColorStateList CorLidaBotao;

                CorLidaBotao = v.getBackgroundTintList();
                if(CorLidaBotao == ColorStateList.valueOf(Color.RED)){
                    // Botão SM2 com cor vermelha

                    String tipoMedidor = "";
                    String NumMedidorAux = "";
                    int iUC = 0;
                    for(int z=0;z < medidores.size(); z++){
                        int sTipo = medidores.get(z).tipo;
                        if(sTipo == 2){
                            UCAux = medidores.get(z).unidadeConsumidora;
                            if(UCAux.length() > 8){
                                UCAux = UCAux.substring(UCAux.length()-8, UCAux.length());
                            }
                            etUC.setText(UCAux);
                            NumMedidorAux = medidores.get(z).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Desativacao;

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja desativar o medidor SM2 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                            break;
                        }
                    }
                }
                else if(CorLidaBotao == ColorStateList.valueOf(Color.parseColor("#FF669900"))){
                    // Botão SM1 com cor verde

                    // Testar se está selecionado monofásico
                    if(sTipoLigacao.trim().equals("Nenhum".trim())){
                        Toast.makeText(getApplicationContext(), "Selecione o tipo de ligação antes de ativar.", Toast.LENGTH_SHORT).show();
                        funcaoEmExecucao = TipoOperacao.FIM;
                        return;
                    }
                    else if(sTipoLigacao.trim().equals("Monofásico".trim())){
                        if((etUC.getText().toString().trim().equals("".trim()))||(etIU.getText().toString().trim().equals("".trim()))){
                            Toast.makeText(getApplicationContext(), "Preencha os campos UC, IU antes da ativação", Toast.LENGTH_SHORT).show();
                            funcaoEmExecucao = TipoOperacao.FIM;
                            return;
                        }

                        String tipoMedidor = "";
                        String NumMedidorAux = "";
                        int iUC = 0;

                        UCAux = etUC.getText().toString();
                        NumMedidorAux = medidores.get(0).numero;
                        etNumeroMedidor.setText(NumMedidorAux);
                        iUC = Integer.parseInt(UCAux);
                        funcaoEmExecucao = TipoOperacao.Ativacao;
                        IndexMedidorAtivar = 2; //SM2

                        AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                android.R.style.Theme_Material_Dialog_Alert);
                        String finalNumMedidorAux = NumMedidorAux;
                        AlertDialog alertDialog = builder
                                .setTitle("Ativação / Desativação")
                                .setMessage("Você deseja ativar o medidor SM2 ?")
                                //.setView(alertText)
                                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        //if (imm != null)
                                        //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                    }
                                })
                                .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        //if (imm != null)
                                        //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        // do nothing
                                    }
                                }).create();
                        alertDialog.getWindow().setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        alertDialog.show();
                    }
                    else if(sTipoLigacao.trim().equals("Bifásico".trim())){
                        if((etUC.getText().toString().trim().equals("".trim()))||(etIU.getText().toString().trim().equals("".trim()))){
                            Toast.makeText(getApplicationContext(), "Preencha os campos UC, IU antes da ativação", Toast.LENGTH_SHORT).show();
                            funcaoEmExecucao = TipoOperacao.FIM;
                            return;
                        }
                        funcaoEmExecucao = TipoOperacao.FIM;
                        if((btSM1.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM3.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM3.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM1 e SM2 livres e SM3 ocupado vai ativar o SB1
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 13; // SB1

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor bifásico SB1 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if(((btSM1.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM1.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))&&
                                (btSM3.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))){
                            // Botão SM2 e SM3 livres e SM1 ocupado vai ativar o SB2
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 14; // SB2

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor bifásico SB2 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM1.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM3.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))){
                            // Botão SM1 e SM3 livres perguntar qual vai ativar
                            AlertDialog.Builder builderSM = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            AlertDialog alertDialogSM = builderSM
                                    .setTitle("Seleção de Medidores")
                                    .setMessage("Você deseja ativar quais medidores ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SB1", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 13; // SB1

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor bifásico SB1 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNegativeButton("SB2", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 14; // SB2

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor bifásico SB2 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    }).create();
                            alertDialogSM.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialogSM.show();
                        }
                        return;
                    }
                    else if(sTipoLigacao.trim().equals("Trifásico".trim())){
                        if((etUC.getText().toString().trim().equals("".trim()))||(etIU.getText().toString().trim().equals("".trim()))){
                            Toast.makeText(getApplicationContext(), "Preencha os campos UC, IU antes da ativação", Toast.LENGTH_SHORT).show();
                            funcaoEmExecucao = TipoOperacao.FIM;
                            return;
                        }
                        funcaoEmExecucao = TipoOperacao.FIM;
                        if((btSM1.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM3.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM4.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM4.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM1, SM2 e SM3 livres e SM4 ocupado vai ativar o ST1
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 24; // ST1

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor trifásico ST1 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if(((btSM1.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM1.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))&&
                                (btSM3.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM4.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))){
                            // Botão SM2, SM3 e SM4 livres e SM1 ocupado vai ativar o ST2
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 25; // ST2

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor trifásico ST2 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM1.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM3.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM4.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))){
                            // Botão SM1, SM2, SM3 e SM4 livres perguntar qual vai ativar
                            AlertDialog.Builder builderSM = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            AlertDialog alertDialogSM = builderSM
                                    .setTitle("Seleção de Medidores")
                                    .setMessage("Você deseja ativar quais medidores ?")
                                    //.setView(alertText)
                                    .setPositiveButton("ST1", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 24; // ST1

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST1 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNegativeButton("ST2", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 25; // ST2

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST2 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    }).create();
                            alertDialogSM.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialogSM.show();
                        }
                        return;
                    }
                }
                //Toast.makeText(getApplicationContext(), "Botão SM2", Toast.LENGTH_SHORT).show();
            }
        });
        //************************* FIM Botão S2 *******************************

        //*********************** INICIO Botão S3 ******************************
        findViewById(R.id.btnS3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UCAux = "";
                ColorStateList CorLidaBotao;

                CorLidaBotao = v.getBackgroundTintList();
                if(CorLidaBotao == ColorStateList.valueOf(Color.RED)){
                    // Botão SM3 com cor vermelha
                    String tipoMedidor = "";
                    String NumMedidorAux = "";
                    int iUC = 0;
                    for(int z=0;z < medidores.size(); z++){
                        int sTipo = medidores.get(z).tipo;
                        if(sTipo == 3){
                            UCAux = medidores.get(z).unidadeConsumidora;
                            if(UCAux.length() > 8){
                                UCAux = UCAux.substring(UCAux.length()-8, UCAux.length());
                            }
                            etUC.setText(UCAux);
                            NumMedidorAux = medidores.get(z).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Desativacao;

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja desativar o medidor SM3 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                            break;
                        }
                    }
                }
                else if(CorLidaBotao == ColorStateList.valueOf(Color.parseColor("#FF669900"))){
                    // Botão SM3 com cor verde

                    // Testar se está selecionado monofásico
                    if(sTipoLigacao.trim().equals("Nenhum".trim())){
                        Toast.makeText(getApplicationContext(), "Selecione o tipo de ligação antes de ativar.", Toast.LENGTH_SHORT).show();
                        funcaoEmExecucao = TipoOperacao.FIM;
                        return;
                    }
                    else if(sTipoLigacao.trim().equals("Monofásico".trim())){
                        if((etUC.getText().toString().trim().equals("".trim()))||(etIU.getText().toString().trim().equals("".trim()))){
                            Toast.makeText(getApplicationContext(), "Preencha os campos UC, IU antes da ativação", Toast.LENGTH_SHORT).show();
                            funcaoEmExecucao = TipoOperacao.FIM;
                            return;
                        }

                        String tipoMedidor = "";
                        String NumMedidorAux = "";
                        int iUC = 0;

                        UCAux = etUC.getText().toString();
                        NumMedidorAux = medidores.get(0).numero;
                        etNumeroMedidor.setText(NumMedidorAux);
                        iUC = Integer.parseInt(UCAux);
                        funcaoEmExecucao = TipoOperacao.Ativacao;
                        IndexMedidorAtivar = 3; // SM3

                        AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                android.R.style.Theme_Material_Dialog_Alert);
                        String finalNumMedidorAux = NumMedidorAux;
                        AlertDialog alertDialog = builder
                                .setTitle("Ativação / Desativação")
                                .setMessage("Você deseja ativar o medidor SM3 ?")
                                //.setView(alertText)
                                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        //if (imm != null)
                                        //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                    }
                                })
                                .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        //if (imm != null)
                                        //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        // do nothing
                                    }
                                }).create();
                        alertDialog.getWindow().setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        alertDialog.show();
                    }
                    else if(sTipoLigacao.trim().equals("Bifásico".trim())){
                        if((etUC.getText().toString().trim().equals("".trim()))||(etIU.getText().toString().trim().equals("".trim()))){
                            Toast.makeText(getApplicationContext(), "Preencha os campos UC, IU antes da ativação", Toast.LENGTH_SHORT).show();
                            funcaoEmExecucao = TipoOperacao.FIM;
                            return;
                        }
                        funcaoEmExecucao = TipoOperacao.FIM;
                        if((btSM2.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM4.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM4.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM2 e SM3 livres e SM4 ocupado vai ativar o SB2
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 14; // SB2

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor bifásico SB2 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if(((btSM2.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM4.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))&&
                                (btSM4.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))){
                            // Botão SM3 e SM4 livres e SM2 ocupado vai ativar o SB3
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 15; // SB3

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor bifásico SB3 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM2.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM4.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))){
                            // Botão SM2 e SM4 livres perguntar qual vai ativar
                            AlertDialog.Builder builderSM = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            AlertDialog alertDialogSM = builderSM
                                    .setTitle("Seleção de Medidores")
                                    .setMessage("Você deseja ativar quais medidores ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SB2", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 14; // SB2

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor bifásico SB2 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNegativeButton("SB3", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 15; // SB3

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor bifásico SB3 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    }).create();
                            alertDialogSM.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialogSM.show();
                        }
                        return;
                    }
                    else if(sTipoLigacao.trim().equals("Trifásico".trim())){
                        if((etUC.getText().toString().trim().equals("".trim()))||(etIU.getText().toString().trim().equals("".trim()))){
                            Toast.makeText(getApplicationContext(), "Preencha os campos UC, IU antes da ativação", Toast.LENGTH_SHORT).show();
                            funcaoEmExecucao = TipoOperacao.FIM;
                            return;
                        }
                        funcaoEmExecucao = TipoOperacao.FIM;
                        if((btSM1.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM2.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM4.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM4.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM1, SM2 e SM3 livres e SM4 ocupado vai ativar o ST1
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 24; // ST1

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor trifásico ST1 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM2.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM4.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM1.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM1.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))&&
                                ((btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM2, SM3 e SM4 livres e SM1 e SM5 ocupado vai ativar o ST1
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 25; // ST2

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor trifásico ST2 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM4.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM1.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM1.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))&&
                                ((btSM2.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM2.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM3, SM4 e SM5 livres e SM1 e SM2 ocupado vai ativar o ST3
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 26; // ST3

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor trifásico ST3 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM4.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM2.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM2.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM3, SM4 e SM5 livres e SM2 ocupado vai ativar o ST3
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 26; // ST3

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor trifásico ST3 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM1.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM2.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM4.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM1, SM2, SM3 e SM4 livres, mas 0 SM5 ocupado, perguntar qual vai ativar
                            AlertDialog.Builder builderSM = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            AlertDialog alertDialogSM = builderSM
                                    .setTitle("Seleção de Medidores")
                                    .setMessage("Você deseja ativar quais medidores ?")
                                    //.setView(alertText)
                                    .setPositiveButton("ST1", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 24; // ST1

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST1 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNegativeButton("ST2", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 25; // ST2

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST2 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    }).create();
                            alertDialogSM.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialogSM.show();
                        }
                        else if((btSM2.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM4.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM1.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM1.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM2, SM3, SM4 e SM5 livres, mas 0 SM1 ocupado, perguntar qual vai ativar
                            AlertDialog.Builder builderSM = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            AlertDialog alertDialogSM = builderSM
                                    .setTitle("Seleção de Medidores")
                                    .setMessage("Você deseja ativar quais medidores ?")
                                    //.setView(alertText)
                                    .setPositiveButton("ST2", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 25; // ST2

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST2 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNegativeButton("ST3", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 26; // ST3

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST3 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    }).create();
                            alertDialogSM.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialogSM.show();
                        }
                        else if((btSM1.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM2.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM4.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))){
                            // Botão SM1, SM2, SM3, SM4 e SM5 livres, perguntar qual vai ativar
                            AlertDialog.Builder builderSM = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            AlertDialog alertDialogSM = builderSM
                                    .setTitle("Seleção de Medidores")
                                    .setMessage("Você deseja ativar quais medidores ?")
                                    //.setView(alertText)
                                    .setPositiveButton("ST1", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 24; // ST1

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST1 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNegativeButton("ST2", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 25; // ST2

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST2 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNeutralButton("ST3", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 26; // ST3

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST3 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    }).create();
                            alertDialogSM.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialogSM.show();
                        }
                        return;
                    }
                }
                //Toast.makeText(getApplicationContext(), "Botão SM3", Toast.LENGTH_SHORT).show();
            }
        });
        //************************* FIM Botão S3 *******************************

        //*********************** INICIO Botão S4 ******************************
        findViewById(R.id.btnS4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UCAux = "";
                ColorStateList CorLidaBotao;

                CorLidaBotao = v.getBackgroundTintList();
                if((CorLidaBotao == ColorStateList.valueOf(Color.RED))||(CorLidaBotao == ColorStateList.valueOf(Color.GRAY))){
                    // Botão SM4 com cor vermelha ou Cinza
                    String tipoMedidor = "";
                    String NumMedidorAux = "";
                    int iUC = 0;
                    for(int z=0;z < medidores.size(); z++){
                        int sTipo = medidores.get(z).tipo;
                        if(sTipo == 4){
                            UCAux = medidores.get(z).unidadeConsumidora;
                            if(UCAux.length() > 8){
                                UCAux = UCAux.substring(UCAux.length()-8, UCAux.length());
                            }
                            etUC.setText(UCAux);
                            NumMedidorAux = medidores.get(z).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Desativacao;

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja desativar o medidor SM4 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                            break;
                        }
                    }
                }
                else if(CorLidaBotao == ColorStateList.valueOf(Color.parseColor("#FF669900"))){
                    // Botão SM4 com cor verde

                    // Testar se está selecionado monofásico
                    if(sTipoLigacao.trim().equals("Nenhum".trim())){
                        Toast.makeText(getApplicationContext(), "Selecione o tipo de ligação antes de ativar.", Toast.LENGTH_SHORT).show();
                        funcaoEmExecucao = TipoOperacao.FIM;
                        return;
                    }
                    else if(sTipoLigacao.trim().equals("Monofásico".trim())){
                        if((etUC.getText().toString().trim().equals("".trim()))||(etIU.getText().toString().trim().equals("".trim()))){
                            Toast.makeText(getApplicationContext(), "Preencha os campos UC, IU antes da ativação", Toast.LENGTH_SHORT).show();
                            funcaoEmExecucao = TipoOperacao.FIM;
                            return;
                        }

                        String tipoMedidor = "";
                        String NumMedidorAux = "";
                        int iUC = 0;

                        UCAux = etUC.getText().toString();
                        NumMedidorAux = medidores.get(0).numero;
                        etNumeroMedidor.setText(NumMedidorAux);
                        iUC = Integer.parseInt(UCAux);
                        //etIU.setText();
                        funcaoEmExecucao = TipoOperacao.Ativacao;
                        IndexMedidorAtivar = 4; // SM4

                        //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                        //final EditText password = alertText.findViewById(R.id.alert_text);

                        AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                android.R.style.Theme_Material_Dialog_Alert);
                        String finalNumMedidorAux = NumMedidorAux;
                        AlertDialog alertDialog = builder
                                .setTitle("Ativação / Desativação")
                                .setMessage("Você deseja ativar o medidor SM4 ?")
                                //.setView(alertText)
                                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        //if (imm != null)
                                        //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                    }
                                })
                                .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        //if (imm != null)
                                        //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        // do nothing
                                    }
                                }).create();
                        alertDialog.getWindow().setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        alertDialog.show();
                    }
                    else if(sTipoLigacao.trim().equals("Bifásico".trim())){
                        if((etUC.getText().toString().trim().equals("".trim()))||(etIU.getText().toString().trim().equals("".trim()))){
                            Toast.makeText(getApplicationContext(), "Preencha os campos UC, IU antes da ativação", Toast.LENGTH_SHORT).show();
                            funcaoEmExecucao = TipoOperacao.FIM;
                            return;
                        }
                        funcaoEmExecucao = TipoOperacao.FIM;
                        if((btSM3.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM3 e SM4 livres e SM5 ocupado vai ativar o SB3
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 15; // SB3

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor bifásico SB3 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if(((btSM3.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM3.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))&&
                                (btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))){
                            // Botão SM4 e SM5 livres e SM3 ocupado vai ativar o SB4
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 16; // SB4

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor bifásico SB4 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM3.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))){
                            // Botão SM3 e SM5 livres perguntar qual vai ativar
                            AlertDialog.Builder builderSM = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            AlertDialog alertDialogSM = builderSM
                                    .setTitle("Seleção de Medidores")
                                    .setMessage("Você deseja ativar quais medidores ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SB3", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 15; // SB3

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor bifásico SB3 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNegativeButton("SB4", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 16; // SB4

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor bifásico SB4 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    }).create();
                            alertDialogSM.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialogSM.show();
                        }
                        return;
                    }
                    else if(sTipoLigacao.trim().equals("Trifásico".trim())){
                        if((etUC.getText().toString().trim().equals("".trim()))||(etIU.getText().toString().trim().equals("".trim()))){
                            Toast.makeText(getApplicationContext(), "Preencha os campos UC, IU antes da ativação", Toast.LENGTH_SHORT).show();
                            funcaoEmExecucao = TipoOperacao.FIM;
                            return;
                        }
                        funcaoEmExecucao = TipoOperacao.FIM;
                        if((btSM2.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM3.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM2, SM3 e SM4 livres e SM5 ocupado vai ativar o ST2
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 25; // ST2

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor trifásico ST2 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM3.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM2.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM2.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))&&
                                ((btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM3, SM4 e SM5 livres e SM2 e SM6 ocupado vai ativar o ST3
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 26; // ST3

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor trifásico ST3 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM2.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM2.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))&&
                                ((btSM3.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM3.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM4, SM5 e SM6 livres e SM2 e SM3 ocupado vai ativar o ST4
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 27; // ST4

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor trifásico ST4 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM3.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM3.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM4, SM5 e SM6 livres e SM3 ocupado vai ativar o ST4
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 27; // ST4

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor trifásico ST4 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM2.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM3.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM2, SM3, SM4 e SM5 livres, mas 0 SM6 ocupado, perguntar qual vai ativar
                            AlertDialog.Builder builderSM = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            AlertDialog alertDialogSM = builderSM
                                    .setTitle("Seleção de Medidores")
                                    .setMessage("Você deseja ativar quais medidores ?")
                                    //.setView(alertText)
                                    .setPositiveButton("ST2", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 25; // ST2

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST2 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNegativeButton("ST3", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 26; // ST3

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST3 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    }).create();
                            alertDialogSM.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialogSM.show();
                        }
                        else if((btSM3.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM2.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM2.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM3, SM4, SM5 e SM6 livres, mas 0 SM2 ocupado, perguntar qual vai ativar
                            AlertDialog.Builder builderSM = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            AlertDialog alertDialogSM = builderSM
                                    .setTitle("Seleção de Medidores")
                                    .setMessage("Você deseja ativar quais medidores ?")
                                    //.setView(alertText)
                                    .setPositiveButton("ST3", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 26; // ST3

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST3 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNegativeButton("ST4", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 27; // ST4

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST4 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    }).create();
                            alertDialogSM.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialogSM.show();
                        }
                        else if((btSM2.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM3.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))){
                            // Botão SM2, SM3, SM4, SM5 e SM6 livres, perguntar qual vai ativar
                            AlertDialog.Builder builderSM = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            AlertDialog alertDialogSM = builderSM
                                    .setTitle("Seleção de Medidores")
                                    .setMessage("Você deseja ativar quais medidores ?")
                                    //.setView(alertText)
                                    .setPositiveButton("ST2", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 25; // ST2

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST2 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNegativeButton("ST3", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 26; // ST3

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST3 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNeutralButton("ST4", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 27; // ST4

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST4 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    }).create();
                            alertDialogSM.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialogSM.show();
                        }
                        return;
                    }
                }
                //Toast.makeText(getApplicationContext(), "Botão SM4", Toast.LENGTH_SHORT).show();
            }
        });
        //************************* FIM Botão S4 *******************************

        //*********************** INICIO Botão S5 ******************************
        findViewById(R.id.btnS5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UCAux = "";
                ColorStateList CorLidaBotao;

                CorLidaBotao = v.getBackgroundTintList();
                if(CorLidaBotao == ColorStateList.valueOf(Color.RED)){
                    String tipoMedidor = "";
                    String NumMedidorAux = "";
                    int iUC = 0;
                    for(int z=0;z < medidores.size(); z++){
                        int sTipo = medidores.get(z).tipo;
                        if(sTipo == 5){
                            UCAux = medidores.get(z).unidadeConsumidora;
                            if(UCAux.length() > 8){
                                UCAux = UCAux.substring(UCAux.length()-8, UCAux.length());
                            }
                            etUC.setText(UCAux);
                            NumMedidorAux = medidores.get(z).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Desativacao;

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja desativar o medidor SM5 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                            break;
                        }
                    }
                }
                else if(CorLidaBotao == ColorStateList.valueOf(Color.parseColor("#FF669900"))){
                    // Testar se está selecionado monofásico
                    if(sTipoLigacao.trim().equals("Nenhum".trim())){
                        Toast.makeText(getApplicationContext(), "Selecione o tipo de ligação antes de ativar.", Toast.LENGTH_SHORT).show();
                        funcaoEmExecucao = TipoOperacao.FIM;
                        return;
                    }
                    else if(sTipoLigacao.trim().equals("Monofásico".trim())){
                        if((etUC.getText().toString().trim().equals("".trim()))||(etIU.getText().toString().trim().equals("".trim()))){
                            Toast.makeText(getApplicationContext(), "Preencha os campos UC, IU antes da ativação", Toast.LENGTH_SHORT).show();
                            funcaoEmExecucao = TipoOperacao.FIM;
                            return;
                        }

                        String tipoMedidor = "";
                        String NumMedidorAux = "";
                        int iUC = 0;

                        UCAux = etUC.getText().toString();
                        NumMedidorAux = medidores.get(0).numero;
                        etNumeroMedidor.setText(NumMedidorAux);
                        iUC = Integer.parseInt(UCAux);
                        //etIU.setText();
                        funcaoEmExecucao = TipoOperacao.Ativacao;
                        IndexMedidorAtivar = 5; // SM5

                        //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                        //final EditText password = alertText.findViewById(R.id.alert_text);

                        AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                android.R.style.Theme_Material_Dialog_Alert);
                        String finalNumMedidorAux = NumMedidorAux;
                        AlertDialog alertDialog = builder
                                .setTitle("Ativação / Desativação")
                                .setMessage("Você deseja ativar o medidor SM5 ?")
                                //.setView(alertText)
                                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        //if (imm != null)
                                        //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                    }
                                })
                                .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        //if (imm != null)
                                        //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        // do nothing
                                    }
                                }).create();
                        alertDialog.getWindow().setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        alertDialog.show();
                    }
                    else if(sTipoLigacao.trim().equals("Bifásico".trim())){
                        if((etUC.getText().toString().trim().equals("".trim()))||(etIU.getText().toString().trim().equals("".trim()))){
                            Toast.makeText(getApplicationContext(), "Preencha os campos UC, IU antes da ativação", Toast.LENGTH_SHORT).show();
                            funcaoEmExecucao = TipoOperacao.FIM;
                            return;
                        }
                        funcaoEmExecucao = TipoOperacao.FIM;
                        if((btSM4.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM4 e SM5 livres e SM6 ocupado vai ativar o SB4
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 16; // SB4

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor bifásico SB4 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if(((btSM4.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM4.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))&&
                                (btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))){
                            // Botão SM5 e SM6 livres e SM4 ocupado vai ativar o SB5
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 17; //SB5

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor bifásico SB5 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM4.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))){
                            // Botão SM4 e SM6 livres perguntar qual vai ativar
                            AlertDialog.Builder builderSM = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            AlertDialog alertDialogSM = builderSM
                                    .setTitle("Seleção de Medidores")
                                    .setMessage("Você deseja ativar quais medidores ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SB4", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 16; // SB4

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor bifásico SB4 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNegativeButton("SB5", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 17; // SB5

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor bifásico SB5 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    }).create();
                            alertDialogSM.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialogSM.show();
                        }
                        return;
                    }
                    else if(sTipoLigacao.trim().equals("Trifásico".trim())){
                        if((etUC.getText().toString().trim().equals("".trim()))||(etIU.getText().toString().trim().equals("".trim()))){
                            Toast.makeText(getApplicationContext(), "Preencha os campos UC, IU antes da ativação", Toast.LENGTH_SHORT).show();
                            funcaoEmExecucao = TipoOperacao.FIM;
                            return;
                        }
                        funcaoEmExecucao = TipoOperacao.FIM;
                        if((btSM3.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM4.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM3, SM4 e SM5 livres e SM6 ocupado vai ativar o ST3
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 26; // ST3

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor trifásico ST3 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM4.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM3.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM3.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))&&
                                ((btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM4, SM5 e SM6 livres e SM3 e SM7 ocupado vai ativar o ST4
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 27; // ST4

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor trifásico ST4 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM3.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM3.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))&&
                                ((btSM4.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM4.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM5, SM6 e SM7 livres e SM3 e SM4 ocupado vai ativar o ST5
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 28; // ST5

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor trifásico ST5 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM4.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM4.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM5, SM6 e SM7 livres e SM4 ocupado vai ativar o ST5
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 28; // ST5

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor trifásico ST5 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM3.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM4.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM3, SM4, SM5 e SM6 livres, mas 0 SM7 ocupado, perguntar qual vai ativar
                            AlertDialog.Builder builderSM = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            AlertDialog alertDialogSM = builderSM
                                    .setTitle("Seleção de Medidores")
                                    .setMessage("Você deseja ativar quais medidores ?")
                                    //.setView(alertText)
                                    .setPositiveButton("ST3", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 26; // ST3

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST3 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNegativeButton("ST4", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 27; // ST4

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST4 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    }).create();
                            alertDialogSM.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialogSM.show();
                        }
                        else if((btSM4.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM3.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM3.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM4, SM5, SM6 e SM7 livres, mas 0 SM3 ocupado, perguntar qual vai ativar
                            AlertDialog.Builder builderSM = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            AlertDialog alertDialogSM = builderSM
                                    .setTitle("Seleção de Medidores")
                                    .setMessage("Você deseja ativar quais medidores ?")
                                    //.setView(alertText)
                                    .setPositiveButton("ST4", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 27; // ST4

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST4 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNegativeButton("ST5", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 28; // ST5

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST5 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    }).create();
                            alertDialogSM.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialogSM.show();
                        }
                        else if((btSM3.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM4.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))){
                            // Botão SM3, SM4, SM5, SM6 e SM7 livres, perguntar qual vai ativar
                            AlertDialog.Builder builderSM = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            AlertDialog alertDialogSM = builderSM
                                    .setTitle("Seleção de Medidores")
                                    .setMessage("Você deseja ativar quais medidores ?")
                                    //.setView(alertText)
                                    .setPositiveButton("ST3", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 26; // ST3

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST3 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNegativeButton("ST4", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 27; // ST4

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST4 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNeutralButton("ST5", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 28; // ST5

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST5 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    }).create();
                            alertDialogSM.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialogSM.show();
                        }
                        return;
                    }
                }
                //Toast.makeText(getApplicationContext(), "Botão SM5", Toast.LENGTH_SHORT).show();
            }
        });
        //************************* FIM Botão S5 *******************************

        //*********************** INICIO Botão S6 ******************************
        findViewById(R.id.btnS6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UCAux = "";
                ColorStateList CorLidaBotao;

                CorLidaBotao = v.getBackgroundTintList();
                if(CorLidaBotao == ColorStateList.valueOf(Color.RED)){
                    String tipoMedidor = "";
                    String NumMedidorAux = "";
                    int iUC = 0;
                    for(int z=0;z < medidores.size(); z++){
                        int sTipo = medidores.get(z).tipo;
                        if(sTipo == 6){
                            UCAux = medidores.get(z).unidadeConsumidora;
                            if(UCAux.length() > 8){
                                UCAux = UCAux.substring(UCAux.length()-8, UCAux.length());
                            }
                            etUC.setText(UCAux);
                            NumMedidorAux = medidores.get(z).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Desativacao;

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja desativar o medidor SM6 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                            break;
                        }
                    }
                }
                else if(CorLidaBotao == ColorStateList.valueOf(Color.parseColor("#FF669900"))){
                    // Testar se está selecionado monofásico
                    if(sTipoLigacao.trim().equals("Nenhum".trim())){
                        Toast.makeText(getApplicationContext(), "Selecione o tipo de ligação antes de ativar.", Toast.LENGTH_SHORT).show();
                        funcaoEmExecucao = TipoOperacao.FIM;
                        return;
                    }
                    else if(sTipoLigacao.trim().equals("Monofásico".trim())){
                        if((etUC.getText().toString().trim().equals("".trim()))||(etIU.getText().toString().trim().equals("".trim()))){
                            Toast.makeText(getApplicationContext(), "Preencha os campos UC, IU antes da ativação", Toast.LENGTH_SHORT).show();
                            funcaoEmExecucao = TipoOperacao.FIM;
                            return;
                        }

                        String tipoMedidor = "";
                        String NumMedidorAux = "";
                        int iUC = 0;

                        UCAux = etUC.getText().toString();
                        NumMedidorAux = medidores.get(0).numero;
                        etNumeroMedidor.setText(NumMedidorAux);
                        iUC = Integer.parseInt(UCAux);
                        //etIU.setText();
                        funcaoEmExecucao = TipoOperacao.Ativacao;
                        IndexMedidorAtivar = 6; // SM6

                        //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                        //final EditText password = alertText.findViewById(R.id.alert_text);

                        AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                android.R.style.Theme_Material_Dialog_Alert);
                        String finalNumMedidorAux = NumMedidorAux;
                        AlertDialog alertDialog = builder
                                .setTitle("Ativação / Desativação")
                                .setMessage("Você deseja ativar o medidor SM6 ?")
                                //.setView(alertText)
                                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        //if (imm != null)
                                        //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                    }
                                })
                                .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        //if (imm != null)
                                        //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        // do nothing
                                    }
                                }).create();
                        alertDialog.getWindow().setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        alertDialog.show();
                    }
                    else if(sTipoLigacao.trim().equals("Bifásico".trim())){
                        if((etUC.getText().toString().trim().equals("".trim()))||(etIU.getText().toString().trim().equals("".trim()))){
                            Toast.makeText(getApplicationContext(), "Preencha os campos UC, IU antes da ativação", Toast.LENGTH_SHORT).show();
                            funcaoEmExecucao = TipoOperacao.FIM;
                            return;
                        }
                        funcaoEmExecucao = TipoOperacao.FIM;
                        if((btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM5 e SM6 livres e SM6 ocupado vai ativar o SB5
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 17; // SB5

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor bifásico SB5 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if(((btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))&&
                                (btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))){
                            // Botão SM6 e SM7 livres e SM5 ocupado vai ativar o SB6
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 18; // SB6

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor bifásico SB6 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))){
                            // Botão SM5 e SM6 livres perguntar qual vai ativar
                            AlertDialog.Builder builderSM = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            AlertDialog alertDialogSM = builderSM
                                    .setTitle("Seleção de Medidores")
                                    .setMessage("Você deseja ativar quais medidores ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SB5", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 17; // SB5

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor bifásico SB5 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNegativeButton("SB6", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 18; // SB6

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor bifásico SB6 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    }).create();
                            alertDialogSM.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialogSM.show();
                        }
                        return;
                    }
                    else if(sTipoLigacao.trim().equals("Trifásico".trim())){
                        if((etUC.getText().toString().trim().equals("".trim()))||(etIU.getText().toString().trim().equals("".trim()))){
                            Toast.makeText(getApplicationContext(), "Preencha os campos UC, IU antes da ativação", Toast.LENGTH_SHORT).show();
                            funcaoEmExecucao = TipoOperacao.FIM;
                            return;
                        }
                        funcaoEmExecucao = TipoOperacao.FIM;
                        if((btSM4.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM4, SM5 e SM6 livres e SM7 ocupado vai ativar o ST4
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 27; // ST4

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor trifásico ST4 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM4.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM4.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))&&
                                ((btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM5, SM6 e SM7 livres e SM4 e SM8 ocupado vai ativar o ST5
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 28; // ST5

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor trifásico ST5 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM4.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM4.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))&&
                                ((btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM6, SM7 e SM8 livres e SM4 e SM5 ocupado vai ativar o ST6
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 29; // ST6

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor trifásico ST6 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM6, SM7 e SM8 livres e SM5 ocupado vai ativar o ST6
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 29; // ST6

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor trifásico ST6 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM4.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM4, SM5, SM6 e SM7 livres, mas 0 SM8 ocupado, perguntar qual vai ativar
                            AlertDialog.Builder builderSM = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            AlertDialog alertDialogSM = builderSM
                                    .setTitle("Seleção de Medidores")
                                    .setMessage("Você deseja ativar quais medidores ?")
                                    //.setView(alertText)
                                    .setPositiveButton("ST4", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 27; // ST4

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST4 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNegativeButton("ST5", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 28; // ST5

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST5 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    }).create();
                            alertDialogSM.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialogSM.show();
                        }
                        else if((btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM4.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM4.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM5, SM6, SM7 e SM8 livres, mas 0 SM4 ocupado, perguntar qual vai ativar
                            AlertDialog.Builder builderSM = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            AlertDialog alertDialogSM = builderSM
                                    .setTitle("Seleção de Medidores")
                                    .setMessage("Você deseja ativar quais medidores ?")
                                    //.setView(alertText)
                                    .setPositiveButton("ST5", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 28; // ST5

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST5 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNegativeButton("ST6", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 29; // ST6

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST6 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    }).create();
                            alertDialogSM.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialogSM.show();
                        }
                        else if((btSM4.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))){
                            // Botão SM4, SM5, SM6, SM7 e SM8 livres, perguntar qual vai ativar
                            AlertDialog.Builder builderSM = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            AlertDialog alertDialogSM = builderSM
                                    .setTitle("Seleção de Medidores")
                                    .setMessage("Você deseja ativar quais medidores ?")
                                    //.setView(alertText)
                                    .setPositiveButton("ST4", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 27; // ST4

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST4 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNegativeButton("ST5", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 28; // ST5

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST5 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNeutralButton("ST6", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 29; // ST6

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST6 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    }).create();
                            alertDialogSM.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialogSM.show();
                        }
                        return;
                    }
                }
                //Toast.makeText(getApplicationContext(), "Botão SM6", Toast.LENGTH_SHORT).show();
            }
        });
        //************************* FIM Botão SM6 *******************************

        //*********************** INICIO Botão SM7 ******************************
        findViewById(R.id.btnS7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UCAux = "";
                ColorStateList CorLidaBotao;

                CorLidaBotao = v.getBackgroundTintList();
                if(CorLidaBotao == ColorStateList.valueOf(Color.RED)){
                    String tipoMedidor = "";
                    String NumMedidorAux = "";
                    int iUC = 0;
                    for(int z=0;z < medidores.size(); z++){
                        int sTipo = medidores.get(z).tipo;
                        if(sTipo == 7){
                            UCAux = medidores.get(z).unidadeConsumidora;
                            if(UCAux.length() > 8){
                                UCAux = UCAux.substring(UCAux.length()-8, UCAux.length());
                            }
                            etUC.setText(UCAux);
                            NumMedidorAux = medidores.get(z).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Desativacao;

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja desativar o medidor SM7 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                            break;
                        }
                    }
                }
                else if(CorLidaBotao == ColorStateList.valueOf(Color.parseColor("#FF669900"))){
                    // Testar se está selecionado monofásico
                    if(sTipoLigacao.trim().equals("Nenhum".trim())){
                        Toast.makeText(getApplicationContext(), "Selecione o tipo de ligação antes de ativar.", Toast.LENGTH_SHORT).show();
                        funcaoEmExecucao = TipoOperacao.FIM;
                        return;
                    }
                    else if(sTipoLigacao.trim().equals("Monofásico".trim())){
                        if((etUC.getText().toString().trim().equals("".trim()))||(etIU.getText().toString().trim().equals("".trim()))){
                            Toast.makeText(getApplicationContext(), "Preencha os campos UC, IU antes da ativação", Toast.LENGTH_SHORT).show();
                            funcaoEmExecucao = TipoOperacao.FIM;
                            return;
                        }

                        String tipoMedidor = "";
                        String NumMedidorAux = "";
                        int iUC = 0;

                        UCAux = etUC.getText().toString();
                        NumMedidorAux = medidores.get(0).numero;
                        etNumeroMedidor.setText(NumMedidorAux);
                        iUC = Integer.parseInt(UCAux);
                        //etIU.setText();
                        funcaoEmExecucao = TipoOperacao.Ativacao;
                        IndexMedidorAtivar = 7; // SM7

                        //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                        //final EditText password = alertText.findViewById(R.id.alert_text);

                        AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                android.R.style.Theme_Material_Dialog_Alert);
                        String finalNumMedidorAux = NumMedidorAux;
                        AlertDialog alertDialog = builder
                                .setTitle("Ativação / Desativação")
                                .setMessage("Você deseja ativar o medidor SM7 ?")
                                //.setView(alertText)
                                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        //if (imm != null)
                                        //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                    }
                                })
                                .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        //if (imm != null)
                                        //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        // do nothing
                                    }
                                }).create();
                        alertDialog.getWindow().setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        alertDialog.show();
                    }
                    else if(sTipoLigacao.trim().equals("Bifásico".trim())){
                        if((etUC.getText().toString().trim().equals("".trim()))||(etIU.getText().toString().trim().equals("".trim()))){
                            Toast.makeText(getApplicationContext(), "Preencha os campos UC, IU antes da ativação", Toast.LENGTH_SHORT).show();
                            funcaoEmExecucao = TipoOperacao.FIM;
                            return;
                        }
                        funcaoEmExecucao = TipoOperacao.FIM;
                        if((btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))){
                            // Botão SM6 e SM7 livres e SM8 ocupado vai ativar o SB6
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 18; // SB6

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor bifásico SB6 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))&&
                                (btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))){
                            // Botão SM7 e SM8 livres e SM6 ocupado vai ativar o SB7
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 19; // SB7

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor bifásico SB7 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))){
                            // Botão SM6 e SM8 livres perguntar qual vai ativar
                            AlertDialog.Builder builderSM = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            AlertDialog alertDialogSM = builderSM
                                    .setTitle("Seleção de Medidores")
                                    .setMessage("Você deseja ativar quais medidores ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SB6", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 18; // SB6

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor bifásico SB6 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNegativeButton("SB7", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 19; // SB7

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor bifásico SB7 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    }).create();
                            alertDialogSM.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialogSM.show();
                        }
                        return;
                    }
                    else if(sTipoLigacao.trim().equals("Trifásico".trim())){
                        if((etUC.getText().toString().trim().equals("".trim()))||(etIU.getText().toString().trim().equals("".trim()))){
                            Toast.makeText(getApplicationContext(), "Preencha os campos UC, IU antes da ativação", Toast.LENGTH_SHORT).show();
                            funcaoEmExecucao = TipoOperacao.FIM;
                            return;
                        }
                        funcaoEmExecucao = TipoOperacao.FIM;
                        if((btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM5, SM6 e SM7 livres e SM8 ocupado vai ativar o ST5
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 28; // ST5

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor trifásico ST5 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))&&
                                ((btSM9.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM9.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM6, SM7 e SM8 livres e SM5 e SM9 ocupado vai ativar o ST6
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 29; // ST6

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor trifásico ST6 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM9.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))&&
                                ((btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM7, SM8 e SM9 livres e SM5 e SM6 ocupado vai ativar o ST7
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 30; // ST7

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor trifásico ST7 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM9.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM7, SM8 e SM9 livres e SM6 ocupado vai ativar o ST7
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 30; // ST7

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor trifásico ST7 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM9.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM9.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM5, SM6, SM7 e SM8 livres, mas 0 SM9 ocupado, perguntar qual vai ativar
                            AlertDialog.Builder builderSM = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            AlertDialog alertDialogSM = builderSM
                                    .setTitle("Seleção de Medidores")
                                    .setMessage("Você deseja ativar quais medidores ?")
                                    //.setView(alertText)
                                    .setPositiveButton("ST5", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 28; // ST5

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST5 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNegativeButton("ST6", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 29; // ST6

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST6 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    }).create();
                            alertDialogSM.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialogSM.show();
                        }
                        else if((btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM9.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM6, SM7, SM8 e SM9 livres, mas 0 SM5 ocupado, perguntar qual vai ativar
                            AlertDialog.Builder builderSM = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            AlertDialog alertDialogSM = builderSM
                                    .setTitle("Seleção de Medidores")
                                    .setMessage("Você deseja ativar quais medidores ?")
                                    //.setView(alertText)
                                    .setPositiveButton("ST6", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 29; // ST6

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST6 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNegativeButton("ST7", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 30; // ST7

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST7 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    }).create();
                            alertDialogSM.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialogSM.show();
                        }
                        else if((btSM5.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM9.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))){
                            // Botão SM4, SM5, SM6, SM7 e SM8 livres, perguntar qual vai ativar
                            AlertDialog.Builder builderSM = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            AlertDialog alertDialogSM = builderSM
                                    .setTitle("Seleção de Medidores")
                                    .setMessage("Você deseja ativar quais medidores ?")
                                    //.setView(alertText)
                                    .setPositiveButton("ST5", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 28; // ST5

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST5 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNegativeButton("ST6", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 29; // ST6

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST6 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNeutralButton("ST7", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 30; // ST7

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST7 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    }).create();
                            alertDialogSM.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialogSM.show();
                        }
                        return;
                    }
                }
                //Toast.makeText(getApplicationContext(), "Botão SM7", Toast.LENGTH_SHORT).show();
            }
        });
        //************************* FIM Botão SM7 *******************************

        //*********************** INICIO Botão SM8 ******************************
        findViewById(R.id.btnS8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UCAux = "";
                ColorStateList CorLidaBotao;

                CorLidaBotao = v.getBackgroundTintList();
                if(CorLidaBotao == ColorStateList.valueOf(Color.RED)){
                    String tipoMedidor = "";
                    String NumMedidorAux = "";
                    int iUC = 0;
                    for(int z=0;z < medidores.size(); z++){
                        int sTipo = medidores.get(z).tipo;
                        if(sTipo == 8){
                            UCAux = medidores.get(z).unidadeConsumidora;
                            if(UCAux.length() > 8){
                                UCAux = UCAux.substring(UCAux.length()-8, UCAux.length());
                            }
                            etUC.setText(UCAux);
                            NumMedidorAux = medidores.get(z).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Desativacao;

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja desativar o medidor SM8 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                            break;
                        }
                    }
                }
                else if(CorLidaBotao == ColorStateList.valueOf(Color.parseColor("#FF669900"))){
                    // Testar se está selecionado monofásico
                    if(sTipoLigacao.trim().equals("Nenhum".trim())){
                        Toast.makeText(getApplicationContext(), "Selecione o tipo de ligação antes de ativar.", Toast.LENGTH_SHORT).show();
                        funcaoEmExecucao = TipoOperacao.FIM;
                        return;
                    }
                    else if(sTipoLigacao.trim().equals("Monofásico".trim())){
                        if((etUC.getText().toString().trim().equals("".trim()))||(etIU.getText().toString().trim().equals("".trim()))){
                            Toast.makeText(getApplicationContext(), "Preencha os campos UC, IU antes da ativação", Toast.LENGTH_SHORT).show();
                            funcaoEmExecucao = TipoOperacao.FIM;
                            return;
                        }

                        String tipoMedidor = "";
                        String NumMedidorAux = "";
                        int iUC = 0;

                        UCAux = etUC.getText().toString();
                        NumMedidorAux = medidores.get(0).numero;
                        etNumeroMedidor.setText(NumMedidorAux);
                        iUC = Integer.parseInt(UCAux);
                        //etIU.setText();
                        funcaoEmExecucao = TipoOperacao.Ativacao;
                        IndexMedidorAtivar = 8;

                        //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                        //final EditText password = alertText.findViewById(R.id.alert_text);

                        AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                android.R.style.Theme_Material_Dialog_Alert);
                        String finalNumMedidorAux = NumMedidorAux;
                        AlertDialog alertDialog = builder
                                .setTitle("Ativação / Desativação")
                                .setMessage("Você deseja ativar o medidor SM8 ?")
                                //.setView(alertText)
                                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        //if (imm != null)
                                        //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                    }
                                })
                                .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        //if (imm != null)
                                        //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        // do nothing
                                    }
                                }).create();
                        alertDialog.getWindow().setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        alertDialog.show();
                    }
                    else if(sTipoLigacao.trim().equals("Bifásico".trim())){
                        if((etUC.getText().toString().trim().equals("".trim()))||(etIU.getText().toString().trim().equals("".trim()))){
                            Toast.makeText(getApplicationContext(), "Preencha os campos UC, IU antes da ativação", Toast.LENGTH_SHORT).show();
                            funcaoEmExecucao = TipoOperacao.FIM;
                            return;
                        }
                        funcaoEmExecucao = TipoOperacao.FIM;
                        if((btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM9.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))){
                            // Botão SM7 e SM9 livres e SM8 ocupado vai ativar o SB7
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 19; // SB7

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor bifásico SB7 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))&&
                                (btSM9.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))){
                            // Botão SM8 e SM9 livres e SM7 ocupado vai ativar o SB8
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 20; // SB8

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor bifásico SB8 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM9.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))){
                            // Botão SM7 e SM9 livres perguntar qual vai ativar
                            AlertDialog.Builder builderSM = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            AlertDialog alertDialogSM = builderSM
                                    .setTitle("Seleção de Medidores")
                                    .setMessage("Você deseja ativar quais medidores ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SB7", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 19; // SB7

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor bifásico SB7 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNegativeButton("SB8", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 20; // SB8

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor bifásico SB8 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    }).create();
                            alertDialogSM.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialogSM.show();
                        }
                        return;
                    }
                    else if(sTipoLigacao.trim().equals("Trifásico".trim())){
                        if((etUC.getText().toString().trim().equals("".trim()))||(etIU.getText().toString().trim().equals("".trim()))){
                            Toast.makeText(getApplicationContext(), "Preencha os campos UC, IU antes da ativação", Toast.LENGTH_SHORT).show();
                            funcaoEmExecucao = TipoOperacao.FIM;
                            return;
                        }
                        funcaoEmExecucao = TipoOperacao.FIM;
                        if((btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM9.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM9.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM6, SM7 e SM8 livres e SM9 ocupado vai ativar o ST6
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 29; // ST6

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor trifásico ST6 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM9.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))&&
                                ((btSM10.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM10.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM7, SM8 e SM9 livres e SM6 e SM10 ocupado vai ativar o ST7
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 30; // ST7

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor trifásico ST7 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM9.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM10.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))&&
                                ((btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM8, SM9 e SM10 livres e SM6 e SM7 ocupado vai ativar o ST8
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 31; // ST8

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor trifásico ST8 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM9.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM10.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM8, SM9 e SM10 livres e SM7 ocupado vai ativar o ST8
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 31; // ST8

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor trifásico ST8 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM9.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM10.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM10.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM6, SM7, SM8 e SM9 livres, mas 0 SM10 ocupado, perguntar qual vai ativar
                            AlertDialog.Builder builderSM = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            AlertDialog alertDialogSM = builderSM
                                    .setTitle("Seleção de Medidores")
                                    .setMessage("Você deseja ativar quais medidores ?")
                                    //.setView(alertText)
                                    .setPositiveButton("ST6", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 29; // ST6

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST6 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNegativeButton("ST7", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 30; // ST7

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST7 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    }).create();
                            alertDialogSM.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialogSM.show();
                        }
                        else if((btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM9.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM10.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM7, SM8, SM9 e SM10 livres, mas 0 SM6 ocupado, perguntar qual vai ativar
                            AlertDialog.Builder builderSM = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            AlertDialog alertDialogSM = builderSM
                                    .setTitle("Seleção de Medidores")
                                    .setMessage("Você deseja ativar quais medidores ?")
                                    //.setView(alertText)
                                    .setPositiveButton("ST7", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 30; // ST7

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST7 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNegativeButton("ST8", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 31; // ST8

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST8 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    }).create();
                            alertDialogSM.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialogSM.show();
                        }
                        else if((btSM6.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM9.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM10.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))){
                            // Botão SM5, SM6, SM7, SM8 e SM9 livres, perguntar qual vai ativar
                            AlertDialog.Builder builderSM = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            AlertDialog alertDialogSM = builderSM
                                    .setTitle("Seleção de Medidores")
                                    .setMessage("Você deseja ativar quais medidores ?")
                                    //.setView(alertText)
                                    .setPositiveButton("ST6", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 29; // ST6

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST6 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNegativeButton("ST7", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 30; // ST7

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST7 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNeutralButton("ST8", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 31; // ST8

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST8 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    }).create();
                            alertDialogSM.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialogSM.show();
                        }
                        return;
                    }
                }
                //Toast.makeText(getApplicationContext(), "Botão SM8", Toast.LENGTH_SHORT).show();
            }
        });
        //************************* FIM Botão SM8 *******************************

        //*********************** INICIO Botão SM9 ******************************
        findViewById(R.id.btnS9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UCAux = "";
                ColorStateList CorLidaBotao;

                CorLidaBotao = v.getBackgroundTintList();
                if(CorLidaBotao == ColorStateList.valueOf(Color.RED)){
                    String tipoMedidor = "";
                    String NumMedidorAux = "";
                    int iUC = 0;
                    for(int z=0;z < medidores.size(); z++){
                        int sTipo = medidores.get(z).tipo;
                        if(sTipo == 9){
                            UCAux = medidores.get(z).unidadeConsumidora;
                            if(UCAux.length() > 8){
                                UCAux = UCAux.substring(UCAux.length()-8, UCAux.length());
                            }
                            etUC.setText(UCAux);
                            NumMedidorAux = medidores.get(z).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Desativacao;

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja desativar o medidor SM9 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                            break;
                        }
                    }
                }
                else if(CorLidaBotao == ColorStateList.valueOf(Color.parseColor("#FF669900"))){
                    // Testar se está selecionado monofásico
                    if(sTipoLigacao.trim().equals("Nenhum".trim())){
                        Toast.makeText(getApplicationContext(), "Selecione o tipo de ligação antes de ativar.", Toast.LENGTH_SHORT).show();
                        funcaoEmExecucao = TipoOperacao.FIM;
                        return;
                    }
                    else if(sTipoLigacao.trim().equals("Monofásico".trim())){
                        if((etUC.getText().toString().trim().equals("".trim()))||(etIU.getText().toString().trim().equals("".trim()))){
                            Toast.makeText(getApplicationContext(), "Preencha os campos UC, IU antes da ativação", Toast.LENGTH_SHORT).show();
                            funcaoEmExecucao = TipoOperacao.FIM;
                            return;
                        }

                        String tipoMedidor = "";
                        String NumMedidorAux = "";
                        int iUC = 0;

                        UCAux = etUC.getText().toString();
                        NumMedidorAux = medidores.get(0).numero;
                        etNumeroMedidor.setText(NumMedidorAux);
                        iUC = Integer.parseInt(UCAux);
                        //etIU.setText();
                        funcaoEmExecucao = TipoOperacao.Ativacao;
                        IndexMedidorAtivar = 9;

                        //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                        //final EditText password = alertText.findViewById(R.id.alert_text);

                        AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                android.R.style.Theme_Material_Dialog_Alert);
                        String finalNumMedidorAux = NumMedidorAux;
                        AlertDialog alertDialog = builder
                                .setTitle("Ativação / Desativação")
                                .setMessage("Você deseja ativar o medidor SM9 ?")
                                //.setView(alertText)
                                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        //if (imm != null)
                                        //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                    }
                                })
                                .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        //if (imm != null)
                                        //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        // do nothing
                                    }
                                }).create();
                        alertDialog.getWindow().setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        alertDialog.show();
                    }
                    else if(sTipoLigacao.trim().equals("Bifásico".trim())){
                        if((etUC.getText().toString().trim().equals("".trim()))||(etIU.getText().toString().trim().equals("".trim()))){
                            Toast.makeText(getApplicationContext(), "Preencha os campos UC, IU antes da ativação", Toast.LENGTH_SHORT).show();
                            funcaoEmExecucao = TipoOperacao.FIM;
                            return;
                        }
                        funcaoEmExecucao = TipoOperacao.FIM;
                        if((btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM10.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))){
                            // Botão SM8 e SM9 livres e SM8 ocupado vai ativar o SB8
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 20; // SB8

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor bifásico SB8 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))&&
                                (btSM10.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))){
                            // Botão SM9 e SM10 livres e SM8 ocupado vai ativar o SB9
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 21; // SB9

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor bifásico SB9 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM10.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))){
                            // Botão SM8 e SM10 livres perguntar qual vai ativar
                            AlertDialog.Builder builderSM = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            AlertDialog alertDialogSM = builderSM
                                    .setTitle("Seleção de Medidores")
                                    .setMessage("Você deseja ativar quais medidores ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SB8", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 20; // SB8

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor bifásico SB8 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNegativeButton("SB9", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 21; // SB9

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor bifásico SB9 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    }).create();
                            alertDialogSM.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialogSM.show();
                        }
                        return;
                    }
                    else if(sTipoLigacao.trim().equals("Trifásico".trim())){
                        if((etUC.getText().toString().trim().equals("".trim()))||(etIU.getText().toString().trim().equals("".trim()))){
                            Toast.makeText(getApplicationContext(), "Preencha os campos UC, IU antes da ativação", Toast.LENGTH_SHORT).show();
                            funcaoEmExecucao = TipoOperacao.FIM;
                            return;
                        }
                        funcaoEmExecucao = TipoOperacao.FIM;
                        if((btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM10.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM10.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM7, SM8 e SM9 livres e SM10 ocupado vai ativar o ST7
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 30; // ST7

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor trifásico ST7 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM10.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))&&
                                ((btSM11.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM11.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM8, SM9 e SM10 livres e SM7 e SM11 ocupado vai ativar o ST8
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 31; // ST8

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor trifásico ST8 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM10.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM11.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))&&
                                ((btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM9, SM10 e SM11 livres e SM7 e SM8 ocupado vai ativar o ST9
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 32; // ST9

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor trifásico ST9 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM10.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM11.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM9, SM10 e SM11 livres e SM8 ocupado vai ativar o ST9
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 32; // ST9

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor trifásico ST9 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM10.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM11.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM11.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM6, SM7, SM8 e SM9 livres, mas 0 SM10 ocupado, perguntar qual vai ativar
                            AlertDialog.Builder builderSM = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            AlertDialog alertDialogSM = builderSM
                                    .setTitle("Seleção de Medidores")
                                    .setMessage("Você deseja ativar quais medidores ?")
                                    //.setView(alertText)
                                    .setPositiveButton("ST7", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 30; // ST7

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST7 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNegativeButton("ST8", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 31; // ST8

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST8 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    }).create();
                            alertDialogSM.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialogSM.show();
                        }
                        else if((btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM10.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM11.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM7, SM8, SM9 e SM10 livres, mas 0 SM6 ocupado, perguntar qual vai ativar
                            AlertDialog.Builder builderSM = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            AlertDialog alertDialogSM = builderSM
                                    .setTitle("Seleção de Medidores")
                                    .setMessage("Você deseja ativar quais medidores ?")
                                    //.setView(alertText)
                                    .setPositiveButton("ST8", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 31; // ST8

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST8 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNegativeButton("ST9", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 32; // ST9

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST9 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    }).create();
                            alertDialogSM.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialogSM.show();
                        }
                        else if((btSM7.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM10.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM11.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))){
                            // Botão SM6, SM7, SM8, SM9 e SM10 livres, perguntar qual vai ativar
                            AlertDialog.Builder builderSM = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            AlertDialog alertDialogSM = builderSM
                                    .setTitle("Seleção de Medidores")
                                    .setMessage("Você deseja ativar quais medidores ?")
                                    //.setView(alertText)
                                    .setPositiveButton("ST7", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 30; // ST7

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST7 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNegativeButton("ST8", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 31; // ST8

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST8 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNeutralButton("ST9", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 32; // ST9

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST9 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    }).create();
                            alertDialogSM.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialogSM.show();
                        }
                        return;
                    }
                }
                //Toast.makeText(getApplicationContext(), "Botão SM9", Toast.LENGTH_SHORT).show();
            }
        });
        //************************* FIM Botão SM9 *******************************

        //*********************** INICIO Botão SM10 ******************************
        findViewById(R.id.btnS10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UCAux = "";
                ColorStateList CorLidaBotao;

                CorLidaBotao = v.getBackgroundTintList();
                if(CorLidaBotao == ColorStateList.valueOf(Color.RED)){
                    String tipoMedidor = "";
                    String NumMedidorAux = "";
                    int iUC = 0;
                    for(int z=0;z < medidores.size(); z++){
                        int sTipo = medidores.get(z).tipo;
                        if(sTipo == 10){
                            UCAux = medidores.get(z).unidadeConsumidora;
                            if(UCAux.length() > 8){
                                UCAux = UCAux.substring(UCAux.length()-8, UCAux.length());
                            }
                            etUC.setText(UCAux);
                            NumMedidorAux = medidores.get(z).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Desativacao;

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja desativar o medidor SM10 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                            break;
                        }
                    }
                }
                else if(CorLidaBotao == ColorStateList.valueOf(Color.parseColor("#FF669900"))){
                    // Testar se está selecionado monofásico
                    if(sTipoLigacao.trim().equals("Nenhum".trim())){
                        Toast.makeText(getApplicationContext(), "Selecione o tipo de ligação antes de ativar.", Toast.LENGTH_SHORT).show();
                        funcaoEmExecucao = TipoOperacao.FIM;
                        return;
                    }
                    else if(sTipoLigacao.trim().equals("Monofásico".trim())){
                        if((etUC.getText().toString().trim().equals("".trim()))||(etIU.getText().toString().trim().equals("".trim()))){
                            Toast.makeText(getApplicationContext(), "Preencha os campos UC, IU antes da ativação", Toast.LENGTH_SHORT).show();
                            funcaoEmExecucao = TipoOperacao.FIM;
                            return;
                        }

                        String tipoMedidor = "";
                        String NumMedidorAux = "";
                        int iUC = 0;

                        UCAux = etUC.getText().toString();
                        NumMedidorAux = medidores.get(0).numero;
                        etNumeroMedidor.setText(NumMedidorAux);
                        iUC = Integer.parseInt(UCAux);
                        //etIU.setText();
                        funcaoEmExecucao = TipoOperacao.Ativacao;
                        IndexMedidorAtivar = 10;

                        //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                        //final EditText password = alertText.findViewById(R.id.alert_text);

                        AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                android.R.style.Theme_Material_Dialog_Alert);
                        String finalNumMedidorAux = NumMedidorAux;
                        AlertDialog alertDialog = builder
                                .setTitle("Ativação / Desativação")
                                .setMessage("Você deseja ativar o medidor SM10 ?")
                                //.setView(alertText)
                                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        //if (imm != null)
                                        //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                    }
                                })
                                .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        //if (imm != null)
                                        //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        // do nothing
                                    }
                                }).create();
                        alertDialog.getWindow().setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        alertDialog.show();
                    }
                    else if(sTipoLigacao.trim().equals("Bifásico".trim())){
                        if((etUC.getText().toString().trim().equals("".trim()))||(etIU.getText().toString().trim().equals("".trim()))){
                            Toast.makeText(getApplicationContext(), "Preencha os campos UC, IU antes da ativação", Toast.LENGTH_SHORT).show();
                            funcaoEmExecucao = TipoOperacao.FIM;
                            return;
                        }
                        funcaoEmExecucao = TipoOperacao.FIM;
                        if((btSM9.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM11.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))){
                            // Botão SM9 e SM10 livres e SM11 ocupado vai ativar o SB9
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 21; // SB9

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor bifásico SB9 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM9.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))&&
                                (btSM11.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))){
                            // Botão SM10 e SM11 livres e SM9 ocupado vai ativar o SB10
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 22; // SB10

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor bifásico SB10 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM9.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM11.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))){
                            // Botão SM19 e SM11 livres perguntar qual vai ativar
                            AlertDialog.Builder builderSM = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            AlertDialog alertDialogSM = builderSM
                                    .setTitle("Seleção de Medidores")
                                    .setMessage("Você deseja ativar quais medidores ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SB9", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 21; // SB9

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor bifásico SB9 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNegativeButton("SB10", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 22; // SB10

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor bifásico SB10 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    }).create();
                            alertDialogSM.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialogSM.show();
                        }
                        return;
                    }
                    else if(sTipoLigacao.trim().equals("Trifásico".trim())){
                        if((etUC.getText().toString().trim().equals("".trim()))||(etIU.getText().toString().trim().equals("".trim()))){
                            Toast.makeText(getApplicationContext(), "Preencha os campos UC, IU antes da ativação", Toast.LENGTH_SHORT).show();
                            funcaoEmExecucao = TipoOperacao.FIM;
                            return;
                        }
                        funcaoEmExecucao = TipoOperacao.FIM;
                        if((btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM9.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM11.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM11.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM8, SM9 e SM10 livres e SM11 ocupado vai ativar o ST8
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 31; // ST8

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor trifásico ST8 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM9.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM11.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))&&
                                ((btSM12.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM12.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM9, SM10 e SM11 livres e SM8 e SM12 ocupado vai ativar o ST9
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 32; // ST9

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor trifásico ST9 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM11.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM12.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))&&
                                ((btSM9.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM9.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM10, SM11 e SM12 livres e SM8 e SM9 ocupado vai ativar o ST10
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 33; // ST10

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor trifásico ST10 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM11.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM12.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM9.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM9.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM8, SM11 e SM12 livres e SM9 ocupado vai ativar o ST10
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 33; // ST10

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor trifásico ST10 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM9.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM11.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM12.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM12.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM7, SM8, SM9 e SM10 livres, mas 0 SM11 ocupado, perguntar qual vai ativar
                            AlertDialog.Builder builderSM = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            AlertDialog alertDialogSM = builderSM
                                    .setTitle("Seleção de Medidores")
                                    .setMessage("Você deseja ativar quais medidores ?")
                                    //.setView(alertText)
                                    .setPositiveButton("ST8", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 31; // ST8

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST8 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNegativeButton("ST9", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 32; // ST9

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST9 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    }).create();
                            alertDialogSM.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialogSM.show();
                        }
                        else if((btSM9.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM11.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM12.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM8, SM9, SM10 e SM11 livres, mas 0 SM7 ocupado, perguntar qual vai ativar
                            AlertDialog.Builder builderSM = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            AlertDialog alertDialogSM = builderSM
                                    .setTitle("Seleção de Medidores")
                                    .setMessage("Você deseja ativar quais medidores ?")
                                    //.setView(alertText)
                                    .setPositiveButton("ST9", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 32; // ST9

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST9 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNegativeButton("ST10", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 33; // ST10

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST10 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    }).create();
                            alertDialogSM.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialogSM.show();
                        }
                        else if((btSM8.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM9.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM11.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM12.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))){
                            // Botão SM7, SM8, SM9, SM10 e SM11 livres, perguntar qual vai ativar
                            AlertDialog.Builder builderSM = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            AlertDialog alertDialogSM = builderSM
                                    .setTitle("Seleção de Medidores")
                                    .setMessage("Você deseja ativar quais medidores ?")
                                    //.setView(alertText)
                                    .setPositiveButton("ST8", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 31; // ST8

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST8 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNegativeButton("ST9", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 32; // ST9

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST9 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNeutralButton("ST10", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 33; // ST10

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST10 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    }).create();
                            alertDialogSM.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialogSM.show();
                        }
                        return;
                    }
                }
                //Toast.makeText(getApplicationContext(), "Botão SM10", Toast.LENGTH_SHORT).show();
            }
        });
        //************************* FIM Botão SM10 *******************************

        //*********************** INICIO Botão SM11 ******************************
        findViewById(R.id.btnS11).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UCAux = "";
                ColorStateList CorLidaBotao;

                CorLidaBotao = v.getBackgroundTintList();
                if(CorLidaBotao == ColorStateList.valueOf(Color.RED)){
                    String tipoMedidor = "";
                    String NumMedidorAux = "";
                    int iUC = 0;
                    for(int z=0;z < medidores.size(); z++){
                        int sTipo = medidores.get(z).tipo;
                        if(sTipo == 11){
                            UCAux = medidores.get(z).unidadeConsumidora;
                            if(UCAux.length() > 8){
                                UCAux = UCAux.substring(UCAux.length()-8, UCAux.length());
                            }
                            etUC.setText(UCAux);
                            NumMedidorAux = medidores.get(z).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Desativacao;

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja desativar o medidor SM11 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                            break;
                        }
                    }
                }
                else if(CorLidaBotao == ColorStateList.valueOf(Color.parseColor("#FF669900"))){
                    // Testar se está selecionado monofásico
                    if(sTipoLigacao.trim().equals("Nenhum".trim())){
                        Toast.makeText(getApplicationContext(), "Selecione o tipo de ligação antes de ativar.", Toast.LENGTH_SHORT).show();
                        funcaoEmExecucao = TipoOperacao.FIM;
                        return;
                    }
                    else if(sTipoLigacao.trim().equals("Monofásico".trim())){
                        if((etUC.getText().toString().trim().equals("".trim()))||(etIU.getText().toString().trim().equals("".trim()))){
                            Toast.makeText(getApplicationContext(), "Preencha os campos UC, IU antes da ativação", Toast.LENGTH_SHORT).show();
                            funcaoEmExecucao = TipoOperacao.FIM;
                            return;
                        }

                        String tipoMedidor = "";
                        String NumMedidorAux = "";
                        int iUC = 0;

                        UCAux = etUC.getText().toString();
                        NumMedidorAux = medidores.get(0).numero;
                        etNumeroMedidor.setText(NumMedidorAux);
                        iUC = Integer.parseInt(UCAux);
                        //etIU.setText();
                        funcaoEmExecucao = TipoOperacao.Ativacao;
                        IndexMedidorAtivar = 11; // SM11

                        //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                        //final EditText password = alertText.findViewById(R.id.alert_text);

                        AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                android.R.style.Theme_Material_Dialog_Alert);
                        String finalNumMedidorAux = NumMedidorAux;
                        AlertDialog alertDialog = builder
                                .setTitle("Ativação / Desativação")
                                .setMessage("Você deseja ativar o medidor SM11 ?")
                                //.setView(alertText)
                                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        //if (imm != null)
                                        //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                    }
                                })
                                .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        //if (imm != null)
                                        //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        // do nothing
                                    }
                                }).create();
                        alertDialog.getWindow().setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        alertDialog.show();
                    }
                    else if(sTipoLigacao.trim().equals("Bifásico".trim())){
                        if((etUC.getText().toString().trim().equals("".trim()))||(etIU.getText().toString().trim().equals("".trim()))){
                            Toast.makeText(getApplicationContext(), "Preencha os campos UC, IU antes da ativação", Toast.LENGTH_SHORT).show();
                            funcaoEmExecucao = TipoOperacao.FIM;
                            return;
                        }
                        funcaoEmExecucao = TipoOperacao.FIM;
                        if((btSM10.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM12.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))){
                            // Botão SM10 e SM11 livres e SM12 ocupado vai ativar o SB10
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 22; // SB10

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor bifásico SB10 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM10.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))&&
                                (btSM12.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))){
                            // Botão SM11 e SM12 livres e SM10 ocupado vai ativar o SB11
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 23; // SB11

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor bifásico SB11 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM10.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM12.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))){
                            // Botão SM10 e SM12 livres perguntar qual vai ativar
                            AlertDialog.Builder builderSM = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            AlertDialog alertDialogSM = builderSM
                                    .setTitle("Seleção de Medidores")
                                    .setMessage("Você deseja ativar quais medidores ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SB10", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 22; //SB10

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor bifásico SB10 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNegativeButton("SB11", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 23; // SB11

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor bifásico SB11 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    }).create();
                            alertDialogSM.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialogSM.show();
                        }
                        return;
                    }
                    else if(sTipoLigacao.trim().equals("Trifásico".trim())){
                        if((etUC.getText().toString().trim().equals("".trim()))||(etIU.getText().toString().trim().equals("".trim()))){
                            Toast.makeText(getApplicationContext(), "Preencha os campos UC, IU antes da ativação", Toast.LENGTH_SHORT).show();
                            funcaoEmExecucao = TipoOperacao.FIM;
                            return;
                        }
                        funcaoEmExecucao = TipoOperacao.FIM;
                        if((btSM9.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM10.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM12.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM12.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM9, SM10 e SM11 livres e SM12 ocupado vai ativar o ST9
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 32; // ST9

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor trifásico ST9 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM10.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM12.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                ((btSM9.getBackgroundTintList() == ColorStateList.valueOf(Color.RED))||(btSM9.getBackgroundTintList() == ColorStateList.valueOf(Color.GRAY)))){
                            // Botão SM10, SM11 e SM12 livres e SM9 ocupado vai ativar o ST10
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 33; // ST10

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor trifásico ST10 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        else if((btSM9.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM10.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))&&
                                (btSM12.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900")))){
                            // Botão SM7, SM8, SM9 e SM10 livres, mas 0 SM11 ocupado, perguntar qual vai ativar
                            AlertDialog.Builder builderSM = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            AlertDialog alertDialogSM = builderSM
                                    .setTitle("Seleção de Medidores")
                                    .setMessage("Você deseja ativar quais medidores ?")
                                    //.setView(alertText)
                                    .setPositiveButton("ST9", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 32; // ST9

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST9 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    })
                                    .setNegativeButton("ST10", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String UCAux1 = "";
                                            String tipoMedidor = "";
                                            String NumMedidorAux = "";
                                            int iUC = 0;

                                            UCAux1 = etUC.getText().toString();
                                            NumMedidorAux = medidores.get(0).numero;
                                            etNumeroMedidor.setText(NumMedidorAux);
                                            iUC = Integer.parseInt(UCAux1);
                                            funcaoEmExecucao = TipoOperacao.Ativacao;
                                            IndexMedidorAtivar = 33; // ST10

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                                    android.R.style.Theme_Material_Dialog_Alert);
                                            String finalNumMedidorAux = NumMedidorAux;
                                            AlertDialog alertDialog = builder
                                                    .setTitle("Ativação / Desativação")
                                                    .setMessage("Você deseja ativar o medidor trifásico ST10 ?")
                                                    //.setView(alertText)
                                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    })
                                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                        }
                                                    }).create();
                                            alertDialog.getWindow().setSoftInputMode(
                                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                            alertDialog.show();
                                        }
                                    }).create();
                            alertDialogSM.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialogSM.show();
                        }
                        return;
                    }
                }
                //Toast.makeText(getApplicationContext(), "Botão SM11", Toast.LENGTH_SHORT).show();
            }
        });
        //************************* FIM Botão SM111 *******************************

        //*********************** INICIO Botão SM12 ******************************
        findViewById(R.id.btnS12).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UCAux = "";
                ColorStateList CorLidaBotao;

                CorLidaBotao = v.getBackgroundTintList();
                if(CorLidaBotao == ColorStateList.valueOf(Color.RED)){
                    String tipoMedidor = "";
                    String NumMedidorAux = "";
                    int iUC = 0;
                    for(int z=0;z < medidores.size(); z++){
                        int sTipo = medidores.get(z).tipo;
                        if(sTipo == 12){
                            UCAux = medidores.get(z).unidadeConsumidora;
                            if(UCAux.length() > 8){
                                UCAux = UCAux.substring(UCAux.length()-8, UCAux.length());
                            }
                            etUC.setText(UCAux);
                            NumMedidorAux = medidores.get(z).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Desativacao;

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja desativar o medidor SM12 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                            break;
                        }
                    }
                }
                else if(CorLidaBotao == ColorStateList.valueOf(Color.parseColor("#FF669900"))){
                    // Testar se está selecionado monofásico
                    if(sTipoLigacao.trim().equals("Nenhum".trim())){
                        Toast.makeText(getApplicationContext(), "Selecione o tipo de ligação antes de ativar.", Toast.LENGTH_SHORT).show();
                        funcaoEmExecucao = TipoOperacao.FIM;
                        return;
                    }
                    else if(sTipoLigacao.trim().equals("Monofásico".trim())){
                        if((etUC.getText().toString().trim().equals("".trim()))||(etIU.getText().toString().trim().equals("".trim()))){
                            Toast.makeText(getApplicationContext(), "Preencha os campos UC, IU antes da ativação", Toast.LENGTH_SHORT).show();
                            funcaoEmExecucao = TipoOperacao.FIM;
                            return;
                        }

                        String tipoMedidor = "";
                        String NumMedidorAux = "";
                        int iUC = 0;

                        UCAux = etUC.getText().toString();
                        NumMedidorAux = medidores.get(0).numero;
                        etNumeroMedidor.setText(NumMedidorAux);
                        iUC = Integer.parseInt(UCAux);
                        //etIU.setText();
                        funcaoEmExecucao = TipoOperacao.Ativacao;
                        IndexMedidorAtivar = 12; // SM12

                        //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                        //final EditText password = alertText.findViewById(R.id.alert_text);

                        AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                android.R.style.Theme_Material_Dialog_Alert);
                        String finalNumMedidorAux = NumMedidorAux;
                        AlertDialog alertDialog = builder
                                .setTitle("Ativação / Desativação")
                                .setMessage("Você deseja ativar o medidor SM12 ?")
                                //.setView(alertText)
                                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        //if (imm != null)
                                        //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                    }
                                })
                                .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        //if (imm != null)
                                        //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        // do nothing
                                    }
                                }).create();
                        alertDialog.getWindow().setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        alertDialog.show();
                    }
                    else if(sTipoLigacao.trim().equals("Bifásico".trim())){
                        //Toast.makeText(getApplicationContext(), "Ativação Bifásica não implementada.", Toast.LENGTH_SHORT).show();
                        if((etUC.getText().toString().trim().equals("".trim()))||(etIU.getText().toString().trim().equals("".trim()))){
                            Toast.makeText(getApplicationContext(), "Preencha os campos UC, IU antes da ativação", Toast.LENGTH_SHORT).show();
                            funcaoEmExecucao = TipoOperacao.FIM;
                            return;
                        }
                        funcaoEmExecucao = TipoOperacao.FIM;
                        if(btSM11.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900"))){
                            // Botão SM11 e SM12 livres ativa SB11
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 23; // SB11

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor bifásico SB11 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                        return;
                    }
                    else if(sTipoLigacao.trim().equals("Trifásico".trim())){
                        //Toast.makeText(getApplicationContext(), "Ativação Trifásica não implementada.", Toast.LENGTH_SHORT).show();
                        if((etUC.getText().toString().trim().equals("".trim()))||(etIU.getText().toString().trim().equals("".trim()))){
                            Toast.makeText(getApplicationContext(), "Preencha os campos UC, IU antes da ativação", Toast.LENGTH_SHORT).show();
                            funcaoEmExecucao = TipoOperacao.FIM;
                            return;
                        }
                        funcaoEmExecucao = TipoOperacao.FIM;
                        if((btSM10.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900"))) &&
                                (btSM11.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF669900"))))
                        {
                            // Botão SM10, SM11 e SM12 livres
                            String tipoMedidor = "";
                            String NumMedidorAux = "";
                            int iUC = 0;

                            UCAux = etUC.getText().toString();
                            NumMedidorAux = medidores.get(0).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            funcaoEmExecucao = TipoOperacao.Ativacao;
                            IndexMedidorAtivar = 33; // ST10

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja ativar o medidor trifásico ST10 ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                        }
                    }
                }
                //Toast.makeText(getApplicationContext(), "Botão SM12", Toast.LENGTH_SHORT).show();
            }
        });
        //************************* FIM Botão SM12 *******************************
        //************************ Fim dos Botões Bifásicos ***********************

        //********************** Inicio dos Botões Trifásicos *********************
        //*********************** INICIO Botão SB1 ******************************
        findViewById(R.id.btnS0102).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UCAux = "";
                ColorStateList CorLidaBotao;

                CorLidaBotao = v.getBackgroundTintList();
                if(CorLidaBotao == ColorStateList.valueOf(Color.RED)){
                    String tipoMedidor = "";
                    String NumMedidorAux = "";
                    int iUC = 0;
                    for(int z=0;z < medidores.size(); z++){
                        int sTipo = medidores.get(z).tipo;
                        if(sTipo == 13){
                            UCAux = medidores.get(z).unidadeConsumidora;
                            if(UCAux.length() > 8){
                                UCAux = UCAux.substring(UCAux.length()-8, UCAux.length());
                            }
                            etUC.setText(UCAux);
                            NumMedidorAux = medidores.get(z).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Desativacao;

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja desativar o medidor ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                            break;
                        }
                    }
                }
                //Toast.makeText(getApplicationContext(), "Botão SB1", Toast.LENGTH_SHORT).show();
            }
        });
        //************************* FIM Botão SB1 *******************************

        //*********************** INICIO Botão SB2 ******************************
        findViewById(R.id.btnS0203).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UCAux = "";
                ColorStateList CorLidaBotao;

                CorLidaBotao = v.getBackgroundTintList();
                if(CorLidaBotao == ColorStateList.valueOf(Color.RED)){
                    String tipoMedidor = "";
                    String NumMedidorAux = "";
                    int iUC = 0;
                    for(int z=0;z < medidores.size(); z++){
                        int sTipo = medidores.get(z).tipo;
                        if(sTipo == 14){
                            UCAux = medidores.get(z).unidadeConsumidora;
                            if(UCAux.length() > 8){
                                UCAux = UCAux.substring(UCAux.length()-8, UCAux.length());
                            }
                            etUC.setText(UCAux);
                            NumMedidorAux = medidores.get(z).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Desativacao;

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja desativar o medidor ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                            break;
                        }
                    }
                }
                //Toast.makeText(getApplicationContext(), "Botão SB2", Toast.LENGTH_SHORT).show();
            }
        });
        //************************* FIM Botão SB2 *******************************

        //*********************** INICIO Botão SB3 ******************************
        findViewById(R.id.btnS0304).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UCAux = "";
                ColorStateList CorLidaBotao;

                CorLidaBotao = v.getBackgroundTintList();
                if(CorLidaBotao == ColorStateList.valueOf(Color.RED)){
                    String tipoMedidor = "";
                    String NumMedidorAux = "";
                    int iUC = 0;
                    for(int z=0;z < medidores.size(); z++){
                        int sTipo = medidores.get(z).tipo;
                        if(sTipo == 15){
                            UCAux = medidores.get(z).unidadeConsumidora;
                            if(UCAux.length() > 8){
                                UCAux = UCAux.substring(UCAux.length()-8, UCAux.length());
                            }
                            etUC.setText(UCAux);
                            NumMedidorAux = medidores.get(z).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Desativacao;

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja desativar o medidor ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                            break;
                        }
                    }
                }
                //Toast.makeText(getApplicationContext(), "Botão SB3", Toast.LENGTH_SHORT).show();
            }
        });
        //************************* FIM Botão SB3 *******************************

        //*********************** INICIO Botão SB4 ******************************
        findViewById(R.id.btnS0405).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UCAux = "";
                ColorStateList CorLidaBotao;

                CorLidaBotao = v.getBackgroundTintList();
                if(CorLidaBotao == ColorStateList.valueOf(Color.RED)){
                    String tipoMedidor = "";
                    String NumMedidorAux = "";
                    int iUC = 0;
                    for(int z=0;z < medidores.size(); z++){
                        int sTipo = medidores.get(z).tipo;
                        if(sTipo == 16){
                            UCAux = medidores.get(z).unidadeConsumidora;
                            if(UCAux.length() > 8){
                                UCAux = UCAux.substring(UCAux.length()-8, UCAux.length());
                            }
                            etUC.setText(UCAux);
                            NumMedidorAux = medidores.get(z).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Desativacao;

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja desativar o medidor ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                            break;
                        }
                    }
                }
                //Toast.makeText(getApplicationContext(), "Botão SB4", Toast.LENGTH_SHORT).show();
            }
        });
        //************************* FIM Botão SB4 *******************************

        //*********************** INICIO Botão SB5 ******************************
        findViewById(R.id.btnS0506).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UCAux = "";
                ColorStateList CorLidaBotao;

                CorLidaBotao = v.getBackgroundTintList();
                if(CorLidaBotao == ColorStateList.valueOf(Color.RED)){
                    String tipoMedidor = "";
                    String NumMedidorAux = "";
                    int iUC = 0;
                    for(int z=0;z < medidores.size(); z++){
                        int sTipo = medidores.get(z).tipo;
                        if(sTipo == 17){
                            UCAux = medidores.get(z).unidadeConsumidora;
                            if(UCAux.length() > 8){
                                UCAux = UCAux.substring(UCAux.length()-8, UCAux.length());
                            }
                            etUC.setText(UCAux);
                            NumMedidorAux = medidores.get(z).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Desativacao;

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja desativar o medidor ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                            break;
                        }
                    }
                }
                //Toast.makeText(getApplicationContext(), "Botão SB5", Toast.LENGTH_SHORT).show();
            }
        });
        //************************* FIM Botão SB5 *******************************

        //*********************** INICIO Botão SB6 ******************************
        findViewById(R.id.btnS0607).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UCAux = "";
                ColorStateList CorLidaBotao;

                CorLidaBotao = v.getBackgroundTintList();
                if(CorLidaBotao == ColorStateList.valueOf(Color.RED)){
                    String tipoMedidor = "";
                    String NumMedidorAux = "";
                    int iUC = 0;
                    for(int z=0;z < medidores.size(); z++){
                        int sTipo = medidores.get(z).tipo;
                        if(sTipo == 18){
                            UCAux = medidores.get(z).unidadeConsumidora;
                            if(UCAux.length() > 8){
                                UCAux = UCAux.substring(UCAux.length()-8, UCAux.length());
                            }
                            etUC.setText(UCAux);
                            NumMedidorAux = medidores.get(z).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Desativacao;

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja desativar o medidor ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                            break;
                        }
                    }
                }
                //Toast.makeText(getApplicationContext(), "Botão SB6", Toast.LENGTH_SHORT).show();
            }
        });
        //************************* FIM Botão SB6 *******************************

        //*********************** INICIO Botão SB7 ******************************
        findViewById(R.id.btnS0708).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UCAux = "";
                ColorStateList CorLidaBotao;

                CorLidaBotao = v.getBackgroundTintList();
                if(CorLidaBotao == ColorStateList.valueOf(Color.RED)){
                    String tipoMedidor = "";
                    String NumMedidorAux = "";
                    int iUC = 0;
                    for(int z=0;z < medidores.size(); z++){
                        int sTipo = medidores.get(z).tipo;
                        if(sTipo == 19){
                            UCAux = medidores.get(z).unidadeConsumidora;
                            if(UCAux.length() > 8){
                                UCAux = UCAux.substring(UCAux.length()-8, UCAux.length());
                            }
                            etUC.setText(UCAux);
                            NumMedidorAux = medidores.get(z).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Desativacao;

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja desativar o medidor ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                            break;
                        }
                    }
                }
                //Toast.makeText(getApplicationContext(), "Botão SB7", Toast.LENGTH_SHORT).show();
            }
        });
        //************************* FIM Botão SB7 *******************************

        //*********************** INICIO Botão SB8 ******************************
        findViewById(R.id.btnS0809).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UCAux = "";
                ColorStateList CorLidaBotao;

                CorLidaBotao = v.getBackgroundTintList();
                if(CorLidaBotao == ColorStateList.valueOf(Color.RED)){
                    String tipoMedidor = "";
                    String NumMedidorAux = "";
                    int iUC = 0;
                    for(int z=0;z < medidores.size(); z++){
                        int sTipo = medidores.get(z).tipo;
                        if(sTipo == 20){
                            UCAux = medidores.get(z).unidadeConsumidora;
                            if(UCAux.length() > 8){
                                UCAux = UCAux.substring(UCAux.length()-8, UCAux.length());
                            }
                            etUC.setText(UCAux);
                            NumMedidorAux = medidores.get(z).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Desativacao;

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja desativar o medidor ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                            break;
                        }
                    }
                }
                //Toast.makeText(getApplicationContext(), "Botão SB8", Toast.LENGTH_SHORT).show();
            }
        });
        //************************* FIM Botão SB8 *******************************

        //*********************** INICIO Botão SB9 ******************************
        findViewById(R.id.btnS0910).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UCAux = "";
                ColorStateList CorLidaBotao;

                CorLidaBotao = v.getBackgroundTintList();
                if(CorLidaBotao == ColorStateList.valueOf(Color.RED)){
                    String tipoMedidor = "";
                    String NumMedidorAux = "";
                    int iUC = 0;
                    for(int z=0;z < medidores.size(); z++){
                        int sTipo = medidores.get(z).tipo;
                        if(sTipo == 21){
                            UCAux = medidores.get(z).unidadeConsumidora;
                            if(UCAux.length() > 8){
                                UCAux = UCAux.substring(UCAux.length()-8, UCAux.length());
                            }
                            etUC.setText(UCAux);
                            NumMedidorAux = medidores.get(z).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Desativacao;

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja desativar o medidor ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                            break;
                        }
                    }
                }
                //Toast.makeText(getApplicationContext(), "Botão SB9", Toast.LENGTH_SHORT).show();
            }
        });
        //************************* FIM Botão SB9 *******************************

        //*********************** INICIO Botão SB10 ******************************
        findViewById(R.id.btnS1011).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UCAux = "";
                ColorStateList CorLidaBotao;

                CorLidaBotao = v.getBackgroundTintList();
                if(CorLidaBotao == ColorStateList.valueOf(Color.RED)){
                    String tipoMedidor = "";
                    String NumMedidorAux = "";
                    int iUC = 0;
                    for(int z=0;z < medidores.size(); z++){
                        int sTipo = medidores.get(z).tipo;
                        if(sTipo == 22){
                            UCAux = medidores.get(z).unidadeConsumidora;
                            if(UCAux.length() > 8){
                                UCAux = UCAux.substring(UCAux.length()-8, UCAux.length());
                            }
                            etUC.setText(UCAux);
                            NumMedidorAux = medidores.get(z).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Desativacao;

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja desativar o medidor ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                            break;
                        }
                    }
                }
                //Toast.makeText(getApplicationContext(), "Botão SB10", Toast.LENGTH_SHORT).show();
            }
        });
        //************************* FIM Botão SB10 *******************************

        //*********************** INICIO Botão SB11 ******************************
        findViewById(R.id.btnS1112).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UCAux = "";
                ColorStateList CorLidaBotao;

                CorLidaBotao = v.getBackgroundTintList();
                if(CorLidaBotao == ColorStateList.valueOf(Color.RED)){
                    String tipoMedidor = "";
                    String NumMedidorAux = "";
                    int iUC = 0;
                    for(int z=0;z < medidores.size(); z++){
                        int sTipo = medidores.get(z).tipo;
                        if(sTipo == 23){
                            UCAux = medidores.get(z).unidadeConsumidora;
                            if(UCAux.length() > 8){
                                UCAux = UCAux.substring(UCAux.length()-8, UCAux.length());
                            }
                            etUC.setText(UCAux);
                            NumMedidorAux = medidores.get(z).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Desativacao;

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja desativar o medidor ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                            break;
                        }
                    }
                }
                //Toast.makeText(getApplicationContext(), "Botão SB11", Toast.LENGTH_SHORT).show();
            }
        });
        //************************* FIM Botão SB11 *******************************

        //*********************** INICIO Botão ST1 ******************************
        findViewById(R.id.btnS010203).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UCAux = "";
                ColorStateList CorLidaBotao;

                CorLidaBotao = v.getBackgroundTintList();
                if(CorLidaBotao == ColorStateList.valueOf(Color.RED)){
                    String tipoMedidor = "";
                    String NumMedidorAux = "";
                    int iUC = 0;
                    for(int z=0;z < medidores.size(); z++){
                        int sTipo = medidores.get(z).tipo;
                        if(sTipo == 24){
                            UCAux = medidores.get(z).unidadeConsumidora;
                            if(UCAux.length() > 8){
                                UCAux = UCAux.substring(UCAux.length()-8, UCAux.length());
                            }
                            etUC.setText(UCAux);
                            NumMedidorAux = medidores.get(z).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Desativacao;

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja desativar o medidor ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                            break;
                        }
                    }
                }
                //Toast.makeText(getApplicationContext(), "Botão ST1", Toast.LENGTH_SHORT).show();
            }
        });
        //************************* FIM Botão ST1 *******************************

        //*********************** INICIO Botão ST2 ******************************
        findViewById(R.id.btnS020304).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UCAux = "";
                ColorStateList CorLidaBotao;

                CorLidaBotao = v.getBackgroundTintList();
                if(CorLidaBotao == ColorStateList.valueOf(Color.RED)){
                    String tipoMedidor = "";
                    String NumMedidorAux = "";
                    int iUC = 0;
                    for(int z=0;z < medidores.size(); z++){
                        int sTipo = medidores.get(z).tipo;
                        if(sTipo == 25){
                            UCAux = medidores.get(z).unidadeConsumidora;
                            if(UCAux.length() > 8){
                                UCAux = UCAux.substring(UCAux.length()-8, UCAux.length());
                            }
                            etUC.setText(UCAux);
                            NumMedidorAux = medidores.get(z).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Desativacao;

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja desativar o medidor ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                            break;
                        }
                    }
                }
                //Toast.makeText(getApplicationContext(), "Botão ST2", Toast.LENGTH_SHORT).show();
            }
        });
        //************************* FIM Botão ST2 *******************************

        //*********************** INICIO Botão ST3 ******************************
        findViewById(R.id.btnS030405).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UCAux = "";
                ColorStateList CorLidaBotao;

                CorLidaBotao = v.getBackgroundTintList();
                if(CorLidaBotao == ColorStateList.valueOf(Color.RED)){
                    String tipoMedidor = "";
                    String NumMedidorAux = "";
                    int iUC = 0;
                    for(int z=0;z < medidores.size(); z++){
                        int sTipo = medidores.get(z).tipo;
                        if(sTipo == 26){
                            UCAux = medidores.get(z).unidadeConsumidora;
                            if(UCAux.length() > 8){
                                UCAux = UCAux.substring(UCAux.length()-8, UCAux.length());
                            }
                            etUC.setText(UCAux);
                            NumMedidorAux = medidores.get(z).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Desativacao;

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja desativar o medidor ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                            break;
                        }
                    }
                }
                //Toast.makeText(getApplicationContext(), "Botão ST3", Toast.LENGTH_SHORT).show();
            }
        });
        //************************* FIM Botão ST3 *******************************

        //*********************** INICIO Botão ST4 ******************************
        findViewById(R.id.btnS040506).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UCAux = "";
                ColorStateList CorLidaBotao;

                CorLidaBotao = v.getBackgroundTintList();
                if(CorLidaBotao == ColorStateList.valueOf(Color.RED)){
                    String tipoMedidor = "";
                    String NumMedidorAux = "";
                    int iUC = 0;
                    for(int z=0;z < medidores.size(); z++){
                        int sTipo = medidores.get(z).tipo;
                        if(sTipo == 27){
                            UCAux = medidores.get(z).unidadeConsumidora;
                            if(UCAux.length() > 8){
                                UCAux = UCAux.substring(UCAux.length()-8, UCAux.length());
                            }
                            etUC.setText(UCAux);
                            NumMedidorAux = medidores.get(z).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Desativacao;

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja desativar o medidor ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                            break;
                        }
                    }
                }
                //Toast.makeText(getApplicationContext(), "Botão ST4", Toast.LENGTH_SHORT).show();
            }
        });
        //************************* FIM Botão ST4 *******************************

        //*********************** INICIO Botão ST5 ******************************
        findViewById(R.id.btnS050607).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UCAux = "";
                ColorStateList CorLidaBotao;

                CorLidaBotao = v.getBackgroundTintList();
                if(CorLidaBotao == ColorStateList.valueOf(Color.RED)){
                    String tipoMedidor = "";
                    String NumMedidorAux = "";
                    int iUC = 0;
                    for(int z=0;z < medidores.size(); z++){
                        int sTipo = medidores.get(z).tipo;
                        if(sTipo == 28){
                            UCAux = medidores.get(z).unidadeConsumidora;
                            if(UCAux.length() > 8){
                                UCAux = UCAux.substring(UCAux.length()-8, UCAux.length());
                            }
                            etUC.setText(UCAux);
                            NumMedidorAux = medidores.get(z).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Desativacao;

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja desativar o medidor ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                            break;
                        }
                    }
                }
                //Toast.makeText(getApplicationContext(), "Botão ST5", Toast.LENGTH_SHORT).show();
            }
        });
        //************************* FIM Botão ST5 *******************************

        //*********************** INICIO Botão ST6 ******************************
        findViewById(R.id.btnS060708).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UCAux = "";
                ColorStateList CorLidaBotao;

                CorLidaBotao = v.getBackgroundTintList();
                if(CorLidaBotao == ColorStateList.valueOf(Color.RED)){
                    String tipoMedidor = "";
                    String NumMedidorAux = "";
                    int iUC = 0;
                    for(int z=0;z < medidores.size(); z++){
                        int sTipo = medidores.get(z).tipo;
                        if(sTipo == 29){
                            UCAux = medidores.get(z).unidadeConsumidora;
                            if(UCAux.length() > 8){
                                UCAux = UCAux.substring(UCAux.length()-8, UCAux.length());
                            }
                            etUC.setText(UCAux);
                            NumMedidorAux = medidores.get(z).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Desativacao;

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja desativar o medidor ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                            break;
                        }
                    }
                }
                //Toast.makeText(getApplicationContext(), "Botão ST6", Toast.LENGTH_SHORT).show();
            }
        });
        //************************* FIM Botão ST6 *******************************

        //*********************** INICIO Botão ST7 ******************************
        findViewById(R.id.btnS070809).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UCAux = "";
                ColorStateList CorLidaBotao;

                CorLidaBotao = v.getBackgroundTintList();
                if(CorLidaBotao == ColorStateList.valueOf(Color.RED)){
                    String tipoMedidor = "";
                    String NumMedidorAux = "";
                    int iUC = 0;
                    for(int z=0;z < medidores.size(); z++){
                        int sTipo = medidores.get(z).tipo;
                        if(sTipo == 30){
                            UCAux = medidores.get(z).unidadeConsumidora;
                            if(UCAux.length() > 8){
                                UCAux = UCAux.substring(UCAux.length()-8, UCAux.length());
                            }
                            etUC.setText(UCAux);
                            NumMedidorAux = medidores.get(z).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Desativacao;

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja desativar o medidor ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                            break;
                        }
                    }
                }
                //Toast.makeText(getApplicationContext(), "Botão ST7", Toast.LENGTH_SHORT).show();
            }
        });
        //************************* FIM Botão ST7 *******************************

        //*********************** INICIO Botão ST8 ******************************
        findViewById(R.id.btnS080910).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UCAux = "";
                ColorStateList CorLidaBotao;

                CorLidaBotao = v.getBackgroundTintList();
                if(CorLidaBotao == ColorStateList.valueOf(Color.RED)){
                    String tipoMedidor = "";
                    String NumMedidorAux = "";
                    int iUC = 0;
                    for(int z=0;z < medidores.size(); z++){
                        int sTipo = medidores.get(z).tipo;
                        if(sTipo == 31){
                            UCAux = medidores.get(z).unidadeConsumidora;
                            if(UCAux.length() > 8){
                                UCAux = UCAux.substring(UCAux.length()-8, UCAux.length());
                            }
                            etUC.setText(UCAux);
                            NumMedidorAux = medidores.get(z).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Desativacao;

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja desativar o medidor ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                            break;
                        }
                    }
                }
                //Toast.makeText(getApplicationContext(), "Botão ST8", Toast.LENGTH_SHORT).show();
            }
        });
        //************************* FIM Botão ST8 *******************************

        //*********************** INICIO Botão ST9 ******************************
        findViewById(R.id.btnS091011).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UCAux = "";
                ColorStateList CorLidaBotao;

                CorLidaBotao = v.getBackgroundTintList();
                if(CorLidaBotao == ColorStateList.valueOf(Color.RED)){
                    String tipoMedidor = "";
                    String NumMedidorAux = "";
                    int iUC = 0;
                    for(int z=0;z < medidores.size(); z++){
                        int sTipo = medidores.get(z).tipo;
                        if(sTipo == 32){
                            UCAux = medidores.get(z).unidadeConsumidora;
                            if(UCAux.length() > 8){
                                UCAux = UCAux.substring(UCAux.length()-8, UCAux.length());
                            }
                            etUC.setText(UCAux);
                            NumMedidorAux = medidores.get(z).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Desativacao;

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja desativar o medidor ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                            break;
                        }
                    }
                }
                //Toast.makeText(getApplicationContext(), "Botão ST9", Toast.LENGTH_SHORT).show();
            }
        });
        //************************* FIM Botão ST9 *******************************

        //*********************** INICIO Botão ST10 ******************************
        findViewById(R.id.btnS101112).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UCAux = "";
                ColorStateList CorLidaBotao;

                CorLidaBotao = v.getBackgroundTintList();
                if(CorLidaBotao == ColorStateList.valueOf(Color.RED)){
                    String tipoMedidor = "";
                    String NumMedidorAux = "";
                    int iUC = 0;
                    for(int z=0;z < medidores.size(); z++){
                        int sTipo = medidores.get(z).tipo;
                        if(sTipo == 33){
                            UCAux = medidores.get(z).unidadeConsumidora;
                            if(UCAux.length() > 8){
                                UCAux = UCAux.substring(UCAux.length()-8, UCAux.length());
                            }
                            etUC.setText(UCAux);
                            NumMedidorAux = medidores.get(z).numero;
                            etNumeroMedidor.setText(NumMedidorAux);
                            iUC = Integer.parseInt(UCAux);
                            //etIU.setText();
                            funcaoEmExecucao = TipoOperacao.Desativacao;

                            //final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
                            //final EditText password = alertText.findViewById(R.id.alert_text);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                                    android.R.style.Theme_Material_Dialog_Alert);
                            String finalNumMedidorAux = NumMedidorAux;
                            AlertDialog alertDialog = builder
                                    .setTitle("Ativação / Desativação")
                                    .setMessage("Você deseja desativar o medidor ?")
                                    //.setView(alertText)
                                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            enviarAberturaSessao(finalNumMedidorAux, mMetodoNumeroSerial);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                        }
                                    })
                                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            //if (imm != null)
                                            //    imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                                            // do nothing
                                        }
                                    }).create();
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            alertDialog.show();
                            break;
                        }
                    }
                }
                //Toast.makeText(getApplicationContext(), "Botão ST10", Toast.LENGTH_SHORT).show();
            }
        });
        //************************* FIM Botão ST10 *******************************

        //-----------------------------------------------------------------
        //final Intent intent = getIntent();
        //mNumeroMedidor = intent.getStringExtra(Consts.EXTRAS_NUMERO_MEDIDOR);
        //mUnidadeConsumidora = intent.getStringExtra(Consts.EXTRAS_UNIDADE_CONSUMIDORA);
        //mDeviceName = intent.getStringExtra(Consts.EXTRAS_DEVICE_NAME);
        //isEasyTrafo = intent.getBooleanExtra(Consts.EXTRAS_EASY_TRAFO, false);
        //mVersaoMedidor = intent.getStringExtra(Consts.EXTRAS_VERSAO_MEDIDOR);
        //mEstadoTampaMedidor = intent.getStringExtra(Consts.EXTRAS_ESTADO_TAMPA);
        //mMetodoNumeroSerial = intent.getByteExtra(Consts.EXTRAS_METODO_NUMERO_SERIAL, mMetodoNumeroSerial);

        //String formato_unidade = "UC: %s";
        //String formato_medidor = "Medidor: %s";

        //if (isEasyTrafo) {
        //    formato_unidade = "Ponto: %s";
        //}

        //((TextView) findViewById(R.id.dados_medidor)).setText(
        //        String.format(formato_medidor, mNumeroMedidor));
        //((TextView) findViewById(R.id.unidade_consumidora)).setText(
        //        String.format(formato_unidade, mUnidadeConsumidora));

        //findViewById(R.id.voltar_tela).setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        onBackPressed();
        //    }
        //});

        //mSalvar = findViewById(R.id.salvar_dados);
        //mSalvar.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        salvarArquivo();
        //    }
        //});

        //mAtualizar = findViewById(R.id.atualizar_dados);
        //mAtualizar.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        if (mAtualizar.getAnimation() == null) {
        //            iniciarLeituraPeriodica();
        //        } else {
        //            pararLeituraPeriodica();
        //        }
        //    }
        //});
        //mTemperatura = findViewById(R.id.temperatura);
        //mVersao = findViewById(R.id.pagina_fiscal_versao);
        //mEstadoTampa = findViewById(R.id.estado_tampa);
        //mHorarioLocal = findViewById(R.id.horario_local);
        //Date data = Calendar.getInstance().getTime();
        //mHorarioLocal.setText(String.format(getString(R.string.horario_local),
        //        new SimpleDateFormat("dd/MM/yyyy' 'HH:mm:ss").format(data)));
        //mHorarioMedidor = findViewById(R.id.horario_medidor);
        //mTempoPacote = findViewById(R.id.tempo_pacote);
        //mVa = findViewById(R.id.va);
        //mVb = findViewById(R.id.vb);
        //mVc = findViewById(R.id.vc);
        //mVab = findViewById(R.id.vab);
        //mVbc = findViewById(R.id.vbc);
        //mVca = findViewById(R.id.vca);
        //mAnguloVa = findViewById(R.id.angulo_va);
        //mAnguloVb = findViewById(R.id.angulo_vb);
        //mAnguloVc = findViewById(R.id.angulo_vc);
        //mIa = findViewById(R.id.ia);
        //mIb = findViewById(R.id.ib);
        //mIc = findViewById(R.id.ic);
        //mDefasagemIa = findViewById(R.id.defasagem_ia);
        //mDefasagemIb = findViewById(R.id.defasagem_ib);
        //mDefasagemIc = findViewById(R.id.defasagem_ic);
        //mFrequencia = findViewById(R.id.frequencia);
        //mPa = findViewById(R.id.pa);
        //mPb = findViewById(R.id.pb);
        //mPc = findViewById(R.id.pc);
        //mPt = findViewById(R.id.pt);
        //mQa = findViewById(R.id.qa);
        //mQb = findViewById(R.id.qb);
        //mQc = findViewById(R.id.qc);
        //mQt = findViewById(R.id.qt);
        //mSa = findViewById(R.id.sa);
        //mSb = findViewById(R.id.sb);
        //mSc = findViewById(R.id.sc);
        //mSt = findViewById(R.id.st);
        //mFPa = findViewById(R.id.fpa);
        //mFPb = findViewById(R.id.fpb);
        //mFPc = findViewById(R.id.fpc);
        //mFPt = findViewById(R.id.fpt);
        //mCFa = findViewById(R.id.cfa);
        //mCFb = findViewById(R.id.cfb);
        //mCFc = findViewById(R.id.cfc);
        //mCFt = findViewById(R.id.cft);
        //mDhtA = findViewById(R.id.dht_a);
        //mDhtB = findViewById(R.id.dht_b);
        //mDhtC = findViewById(R.id.dht_c);
        //mDhcA = findViewById(R.id.dhc_a);
        //mDhcB = findViewById(R.id.dhc_b);
        //mDhcC = findViewById(R.id.dhc_c);
        //mDiagramaFasorial = findViewById(R.id.diagrama_fasorial);

        bindGattService(mServiceConnection);
    }

    private void iniciarLeituraPeriodica() {
        iniciarAnimacaoAtualizar();

        mAtualizarTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        enviarLeituraGrandezasInstantaneas();
                    }
                });
            }
        };
        mAtualizarTimer.scheduleAtFixedRate(mAtualizarTask, 0, TEMPO_LEITURA_PERIODICA);
    }

    private void pararLeituraPeriodica() {
        pararAnimacaoAtualizar();

        mAtualizarTask.cancel();
        mAtualizarTimer.purge();
    }

    private void iniciarAnimacaoAtualizar() {
        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate_refresh);
        rotation.setRepeatCount(Animation.INFINITE);
        mAtualizar.startAnimation(rotation);
    }

    private void pararAnimacaoAtualizar() {
        mAtualizar.clearAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            setupGattService();
            //iniciarLeituraPeriodica();
        } else {
            mBluetoothLeService = new BluetoothLeService();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //pararLeituraPeriodica();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindGattService(mServiceConnection);
    }

    protected ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            setupGattService();
            //iniciarLeituraPeriodica();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    private void enviaComandoAB03(String num_medidor, byte IndiceMedidorAtivar, byte Comando, byte ativacao, int UC, int IU, int IUTC, int metodo) {

        byte[] comando = new ComandoAbsoluto.AB03()
                .comNumerMedidor(num_medidor, metodo)
                .build(metodo, Comando, ativacao, UC, IU, IUTC, IndiceMedidorAtivar);
        tempoEnvio = sendData(comando);
        Log.d(TAG, "ENS->" + Util.ByteArrayToHexString(comando));
    }

    private void enviaComandoAB04(String num_medidor, byte Comando, byte[] CodMostradores, int metodo) {

        byte[] comando = new ComandoAbsoluto.AB04()
                .comNumerMedidor(num_medidor, metodo)
                .build(metodo, Comando, CodMostradores);
        tempoEnvio = sendData(comando);
        Log.d(TAG, "ENS->" + Util.ByteArrayToHexString(comando));
    }

    private void enviarLeituraGrandezasInstantaneas() {
        byte[] data = new ComandoAbsoluto.ABNT14GrandezasInstantaneas()
                .comMedidorNumero(mNumeroMedidor, mMetodoNumeroSerial)
                .build(!isEasyTrafo, mMetodoNumeroSerial);
        tempoEnvio = sendData(data);
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                final byte[] data = intent.getByteArrayExtra(BluetoothLeService.EXTRA_BYTES);
                try {
                    mByteArrayData.write(data);
                    mByteCount += data.length;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (mByteCount >= 258) {
                    Log.d(TAG, "################# TEMPO DA PAGINA FISCAL: " + Float.toString(System.currentTimeMillis() - tempoEnvio) + "ms");
                    //mTempoPacote.setText("Tempo: " + (System.currentTimeMillis() - tempoEnvio) + "ms");
                    processData();
                    mByteCount = 0;
                    mByteArrayData.reset();
                }
            }
        }
    };

    private void enviarLimparOcorrencia(String numeroMedidor, byte codigoOcorrencia, int metodo) {
        final byte[] comando = new ComandoAbsoluto.LimpezaOcorrenciasMedidor()
                .comMedidorNumero(numeroMedidor, metodo)
                .comCodigoOcorrencia(codigoOcorrencia)
                .build(!isEasyTrafo, metodo);
        sendData(comando);
    }

    private void processarRespostaOcorrencia(RespostaAbsoluto respostaAbsoluto) {
        Toast.makeText(ConfiguracaoMedidor.this,
                getString(R.string.text_ocorrencia_medidor),
                Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Ocorrência: " + respostaAbsoluto.toString());
        enviarLimparOcorrencia(respostaAbsoluto.getSufixoNumeroMedidor(),
                respostaAbsoluto.getCodigoOcorrencia(), mMetodoNumeroSerial);
    }

    private void enviarComando(byte[] data) {
        Log.d(TAG, "ENS->" + Util.ByteArrayToHexString(data));
        if (sendData(data) == 0)
            Log.d(TAG, "Erro ao enviar mensagem");
    }

    private void enviarAberturaSessao(String numeroMedidor, int metodo) {
        if(bSenhaDigitada){
            final String nm = numeroMedidor;
            byte[] comando = new ComandoAbsoluto.AB11SolicitacaoAberturaSessao()
                    .comMedidorNumero(nm, metodo)
                    .comSenha(SenhaDigitada)
                    .build((tipo_medidor == DeviceActivity.TipoMedidor.ABSOLUTO), metodo);
            enviarComando(comando);
        }
        else{
            final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
            final EditText password = alertText.findViewById(R.id.alert_text);

            final String nm = numeroMedidor;
            AlertDialog.Builder builder = new AlertDialog.Builder(this,
                    android.R.style.Theme_Material_Dialog_Alert);
            AlertDialog alertDialog = builder
                    .setTitle("Abertura de sessão")
                    .setMessage("Digite o código de acesso")
                    .setView(alertText)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            byte[] comando = new ComandoAbsoluto.AB11SolicitacaoAberturaSessao()
                                    .comMedidorNumero(nm, metodo)
                                    .comSenha(password.getText().toString())
                                    .build((tipo_medidor == DeviceActivity.TipoMedidor.ABSOLUTO), metodo);
                            SenhaDigitada = password.getText().toString();
                            //bSenhaDigitada = true;
                            enviarComando(comando);
                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                            if (imm != null)
                                imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                            if (imm != null)
                                imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                            // do nothing
                        }
                    }).create();
            alertDialog.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            alertDialog.show();
        }
    }

    private void processData() {
        byte[] data = mByteArrayData.toByteArray();
        RespostaAbsoluto resposta = new RespostaAbsoluto(data);
        if (resposta.isOcorrencia()) {
            processarRespostaOcorrencia(resposta);
        }
        else if (resposta.isRespostaOcorrencias()) {
            //TODO FIM
        }
        else if (resposta.isGrandezasInstantaneas()) {
            processarRespostaGrandezasInstantaneas(resposta);
        } else if(resposta.isAberturaSessao()){
            processarRespostaAberturaSessao(data[7], resposta);
        } else if (resposta.isAB03()) {
            brespostaAB03 = processaAB03(resposta);
        } else if (resposta.isAB04()) {
            brespostaAB04 = processaAB04(resposta);
        } else {
            Log.d(TAG, "Recebeu pacote não tratado: " + Util.ByteArrayToHexString(data));
        }
    }

    private void showErrorMessage(String mensagem) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracaoMedidor.this,
                    android.R.style.Theme_Material_Dialog_Alert);
            builder
                    .setTitle("Erro")
                    .setMessage(mensagem)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }
    }

    private Boolean processaAB03(RespostaAbsoluto respostaAbsoluto){
        String UCLida = "";
        String IULida = "";
        String medidorResposta = "";

        ComandoAbsoluto.AB03 mAB03 = new ComandoAbsoluto.AB03();

        RespostaAbsoluto.LeituraAB03 mLeitura = respostaAbsoluto.interpretaAB03();

        medidorResposta = respostaAbsoluto.getNumeroMedidor();

        Log.i("pcAB03", mLeitura.toString());

        UCLida = Integer.valueOf(mLeitura.UC).toString();
        int TamUC = UCLida.length();
        if(UCLida.length() < 8){
            for(int x=0; x < (8-TamUC); x++){
                UCLida = "0" + UCLida;
            }
        }
        IULida = Long.valueOf(mLeitura.IU).toString();

        if(mLeitura.StatusAtivacao == 0){
            // Desativou ou Está Desativado
            if(funcaoEmExecucao == TipoOperacao.Desativacao){
                // Era pra desativar e desativou
                String medidor = "";
                for(int x=0; x < medidores.size(); x++){
                    medidor = medidores.get(x).numero;
                    if(medidor.trim().equals(medidorResposta.trim())){
                        switch(medidores.get(x).tipo){
                            case 1:
                                // SM1
                                btSM1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                break;
                            case 2:
                                // SM2
                                btSM2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                break;
                            case 3:
                                // SM3
                                btSM3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                break;
                            case 4:
                                // SM4
                                btSM4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                break;
                            case 5:
                                // SM5
                                btSM5.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                break;
                            case 6:
                                // SM6
                                btSM6.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                break;
                            case 7:
                                // SM7
                                btSM7.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                break;
                            case 8:
                                // SM8
                                btSM8.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                break;
                            case 9:
                                // SM9
                                btSM9.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                break;
                            case 10:
                                // SM10
                                btSM10.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                break;
                            case 11:
                                // SM11
                                btSM11.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                break;
                            case 12:
                                // SM12
                                btSM12.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                break;
                            case 13:
                                // SB1
                                btSB1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM1
                                btSM1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM2
                                btSM2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                btSB1.setVisibility(View.GONE);
                                btSM1.setVisibility(View.VISIBLE);
                                btSM1.setEnabled(true);
                                btSM2.setVisibility(View.VISIBLE);
                                btSM2.setEnabled(true);
                                break;
                            case 14:
                                // SB2
                                btSB2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM2
                                btSM2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM3
                                btSM3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                btSB2.setVisibility(View.GONE);
                                btSM2.setVisibility(View.VISIBLE);
                                btSM2.setEnabled(true);
                                btSM3.setVisibility(View.VISIBLE);
                                btSM3.setEnabled(true);
                                break;
                            case 15:
                                // SB3
                                btSB3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM3
                                btSM3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM4
                                btSM4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                btSB3.setVisibility(View.GONE);
                                btSM3.setVisibility(View.VISIBLE);
                                btSM3.setEnabled(true);
                                btSM4.setVisibility(View.VISIBLE);
                                btSM4.setEnabled(true);
                                break;
                            case 16:
                                // SB4
                                btSB4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM4
                                btSM4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM5
                                btSM5.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                btSB4.setVisibility(View.GONE);
                                btSM4.setVisibility(View.VISIBLE);
                                btSM4.setEnabled(true);
                                btSM5.setVisibility(View.VISIBLE);
                                btSM5.setEnabled(true);
                                break;
                            case 17:
                                // SB5
                                btSB5.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM5
                                btSM5.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM6
                                btSM6.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                btSB5.setVisibility(View.GONE);
                                btSM5.setVisibility(View.VISIBLE);
                                btSM5.setEnabled(true);
                                btSM6.setVisibility(View.VISIBLE);
                                btSM6.setEnabled(true);
                                break;
                            case 18:
                                // SB6
                                btSB6.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM6
                                btSM6.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM7
                                btSM7.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                btSB6.setVisibility(View.GONE);
                                btSM6.setVisibility(View.VISIBLE);
                                btSM6.setEnabled(true);
                                btSM7.setVisibility(View.VISIBLE);
                                btSM7.setEnabled(true);
                                break;
                            case 19:
                                // SB7
                                btSB7.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM7
                                btSM7.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM8
                                btSM8.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                btSB7.setVisibility(View.GONE);
                                btSM7.setVisibility(View.VISIBLE);
                                btSM7.setEnabled(true);
                                btSM8.setVisibility(View.VISIBLE);
                                btSM8.setEnabled(true);
                                break;
                            case 20:
                                // SB8
                                btSB8.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM8
                                btSM8.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM9
                                btSM9.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                btSB8.setVisibility(View.GONE);
                                btSM8.setVisibility(View.VISIBLE);
                                btSM8.setEnabled(true);
                                btSM9.setVisibility(View.VISIBLE);
                                btSM9.setEnabled(true);
                                break;
                            case 21:
                                // SB9
                                btSB9.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM9
                                btSM9.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM10
                                btSM10.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                btSB9.setVisibility(View.GONE);
                                btSM9.setVisibility(View.VISIBLE);
                                btSM9.setEnabled(true);
                                btSM10.setVisibility(View.VISIBLE);
                                btSM10.setEnabled(true);
                                break;
                            case 22:
                                // SB10
                                btSB10.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM10
                                btSM10.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM11
                                btSM11.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                btSB10.setVisibility(View.GONE);
                                btSM10.setVisibility(View.VISIBLE);
                                btSM10.setEnabled(true);
                                btSM11.setVisibility(View.VISIBLE);
                                btSM11.setEnabled(true);
                                break;
                            case 23:
                                // SB11
                                btSB11.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM11
                                btSM11.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM12
                                btSM12.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                btSB11.setVisibility(View.GONE);
                                btSM11.setVisibility(View.VISIBLE);
                                btSM11.setEnabled(true);
                                btSM12.setVisibility(View.VISIBLE);
                                btSM12.setEnabled(true);
                                break;
                            case 24:
                                // ST1
                                btST1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM1
                                btSM1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM2
                                btSM2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM3
                                btSM3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                btST1.setVisibility(View.GONE);
                                btSM1.setVisibility(View.VISIBLE);
                                btSM1.setEnabled(true);
                                btSM2.setVisibility(View.VISIBLE);
                                btSM2.setEnabled(true);
                                btSM3.setVisibility(View.VISIBLE);
                                btSM3.setEnabled(true);
                                break;
                            case 25:
                                // ST2
                                btST2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM2
                                btSM2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM3
                                btSM3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM4
                                btSM4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                btST2.setVisibility(View.GONE);
                                btSM2.setVisibility(View.VISIBLE);
                                btSM2.setEnabled(true);
                                btSM3.setVisibility(View.VISIBLE);
                                btSM3.setEnabled(true);
                                btSM4.setVisibility(View.VISIBLE);
                                btSM4.setEnabled(true);
                                break;
                            case 26:
                                // ST3
                                btST3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM3
                                btSM3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM4
                                btSM4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM5
                                btSM5.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                btST3.setVisibility(View.GONE);
                                btSM3.setVisibility(View.VISIBLE);
                                btSM3.setEnabled(true);
                                btSM4.setVisibility(View.VISIBLE);
                                btSM4.setEnabled(true);
                                btSM5.setVisibility(View.VISIBLE);
                                btSM5.setEnabled(true);
                                break;
                            case 27:
                                // ST4
                                btST4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM4
                                btSM4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM5
                                btSM5.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM6
                                btSM6.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                btST4.setVisibility(View.GONE);
                                btSM4.setVisibility(View.VISIBLE);
                                btSM4.setEnabled(true);
                                btSM5.setVisibility(View.VISIBLE);
                                btSM5.setEnabled(true);
                                btSM6.setVisibility(View.VISIBLE);
                                btSM6.setEnabled(true);
                                break;
                            case 28:
                                // ST5
                                btST5.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM5
                                btSM5.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM6
                                btSM6.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM7
                                btSM7.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                btST5.setVisibility(View.GONE);
                                btSM5.setVisibility(View.VISIBLE);
                                btSM5.setEnabled(true);
                                btSM6.setVisibility(View.VISIBLE);
                                btSM6.setEnabled(true);
                                btSM7.setVisibility(View.VISIBLE);
                                btSM7.setEnabled(true);
                                break;
                            case 29:
                                // ST6
                                btST6.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM6
                                btSM6.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM7
                                btSM7.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM8
                                btSM8.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                btST6.setVisibility(View.GONE);
                                btSM6.setVisibility(View.VISIBLE);
                                btSM6.setEnabled(true);
                                btSM7.setVisibility(View.VISIBLE);
                                btSM7.setEnabled(true);
                                btSM8.setVisibility(View.VISIBLE);
                                btSM8.setEnabled(true);
                                break;
                            case 30:
                                // ST7
                                btST7.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM7
                                btSM7.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM8
                                btSM8.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM9
                                btSM9.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                btST7.setVisibility(View.GONE);
                                btSM7.setVisibility(View.VISIBLE);
                                btSM7.setEnabled(true);
                                btSM8.setVisibility(View.VISIBLE);
                                btSM8.setEnabled(true);
                                btSM9.setVisibility(View.VISIBLE);
                                btSM9.setEnabled(true);
                                break;
                            case 31:
                                // ST8
                                btST8.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM8
                                btSM8.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM9
                                btSM9.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM10
                                btSM10.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                btST8.setVisibility(View.GONE);
                                btSM8.setVisibility(View.VISIBLE);
                                btSM8.setEnabled(true);
                                btSM9.setVisibility(View.VISIBLE);
                                btSM9.setEnabled(true);
                                btSM10.setVisibility(View.VISIBLE);
                                btSM10.setEnabled(true);
                                break;
                            case 32:
                                // ST9
                                btST9.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM9
                                btSM9.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM10
                                btSM10.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM11
                                btSM11.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                btST9.setVisibility(View.GONE);
                                btSM9.setVisibility(View.VISIBLE);
                                btSM9.setEnabled(true);
                                btSM10.setVisibility(View.VISIBLE);
                                btSM10.setEnabled(true);
                                btSM11.setVisibility(View.VISIBLE);
                                btSM11.setEnabled(true);
                                break;
                            case 33:
                                // ST10
                                btST10.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM10
                                btSM10.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM11
                                btSM11.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                // SM12
                                btSM12.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF669900")));
                                btST10.setVisibility(View.GONE);
                                btSM10.setVisibility(View.VISIBLE);
                                btSM10.setEnabled(true);
                                btSM11.setVisibility(View.VISIBLE);
                                btSM11.setEnabled(true);
                                btSM12.setVisibility(View.VISIBLE);
                                btSM12.setEnabled(true);
                                break;
                        }
                        medidores.remove(x);
                    }
                }
            }
            else{
                Toast.makeText(this, "Medidor não foi ativado", Toast.LENGTH_SHORT).show();
                IndexMedidorAtivar = 0;
            }
        }else if(mLeitura.StatusAtivacao == 1){
            // Ativou ou Está Ativado
            if(funcaoEmExecucao == TipoOperacao.Ativacao){
                // Era pra ativar e ativou
                String medidor = "";
                switch(IndexMedidorAtivar){
                    case 1:
                        // SM1
                        btSM1.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM1.setEnabled(true);
                        medidor = mMedidoresCom1;
                        break;
                    case 2:
                        // SM2
                        btSM2.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM2.setEnabled(true);
                        medidor = mMedidoresCom2;
                        break;
                    case 3:
                        // SM3
                        btSM3.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM3.setEnabled(true);
                        medidor = mMedidoresCom3;
                        break;
                    case 4:
                        // SM4
                        btSM4.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM4.setEnabled(true);
                        medidor = mMedidoresCom4;
                        break;
                    case 5:
                        // SM5
                        btSM5.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM5.setEnabled(true);
                        medidor = mMedidoresCom5;
                        break;
                    case 6:
                        // SM6
                        btSM6.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM6.setEnabled(true);
                        medidor = mMedidoresCom6;
                        break;
                    case 7:
                        // SM7
                        btSM7.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM7.setEnabled(true);
                        medidor = mMedidoresCom7;
                        break;
                    case 8:
                        // SM8
                        btSM8.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM8.setEnabled(true);
                        medidor = mMedidoresCom8;
                        break;
                    case 9:
                        // SM9
                        btSM9.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM9.setEnabled(true);
                        medidor = mMedidoresCom9;
                        break;
                    case 10:
                        // SM10
                        btSM10.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM10.setEnabled(true);
                        medidor = mMedidoresCom10;
                        break;
                    case 11:
                        // SM11
                        btSM11.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM11.setEnabled(true);
                        medidor = mMedidoresCom11;
                        break;
                    case 12:
                        // SM12
                        btSM12.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM12.setEnabled(true);
                        medidor = mMedidoresCom12;
                        break;
                    case 13:
                        // SB1
                        btSB1.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSB1.setEnabled(true);
                        btSB1.setVisibility(View.VISIBLE);
                        btSM1.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM1.setVisibility(View.GONE);
                        btSM1.setEnabled(false);
                        btSM2.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM2.setVisibility(View.GONE);
                        btSM2.setEnabled(false);
                        medidor = mMedidoresCom1;
                        break;
                    case 14:
                        // SB2
                        btSB2.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSB2.setEnabled(true);
                        btSB2.setVisibility(View.VISIBLE);
                        btSM2.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM2.setVisibility(View.GONE);
                        btSM2.setEnabled(false);
                        btSM3.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM3.setVisibility(View.GONE);
                        btSM3.setEnabled(false);
                        medidor = mMedidoresCom2;
                        break;
                    case 15:
                        // SB3
                        btSB3.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSB3.setEnabled(true);
                        btSB3.setVisibility(View.VISIBLE);
                        btSM3.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM3.setVisibility(View.GONE);
                        btSM3.setEnabled(false);
                        btSM4.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM4.setVisibility(View.GONE);
                        btSM4.setEnabled(false);
                        medidor = mMedidoresCom3;
                        break;
                    case 16:
                        // SB4
                        btSB4.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSB4.setEnabled(true);
                        btSB4.setVisibility(View.VISIBLE);
                        btSM4.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM4.setVisibility(View.GONE);
                        btSM4.setEnabled(false);
                        btSM5.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM5.setVisibility(View.GONE);
                        btSM5.setEnabled(false);
                        medidor = mMedidoresCom4;
                        break;
                    case 17:
                        // SB5
                        btSB5.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSB5.setEnabled(true);
                        btSB5.setVisibility(View.VISIBLE);
                        btSM5.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM5.setVisibility(View.GONE);
                        btSM5.setEnabled(false);
                        btSM6.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM6.setVisibility(View.GONE);
                        btSM6.setEnabled(false);
                        medidor = mMedidoresCom5;
                        break;
                    case 18:
                        // SB6
                        btSB6.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSB6.setEnabled(true);
                        btSB6.setVisibility(View.VISIBLE);
                        btSM6.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM6.setVisibility(View.GONE);
                        btSM6.setEnabled(false);
                        btSM7.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM7.setVisibility(View.GONE);
                        btSM7.setEnabled(false);
                        medidor = mMedidoresCom6;
                        break;
                    case 19:
                        // SB7
                        btSB7.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSB7.setEnabled(true);
                        btSB7.setVisibility(View.VISIBLE);
                        btSM7.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM7.setVisibility(View.GONE);
                        btSM7.setEnabled(false);
                        btSM8.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM8.setVisibility(View.GONE);
                        btSM8.setEnabled(false);
                        medidor = mMedidoresCom7;
                        break;
                    case 20:
                        // SB8
                        btSB8.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSB8.setEnabled(true);
                        btSB8.setVisibility(View.VISIBLE);
                        btSM8.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM8.setVisibility(View.GONE);
                        btSM8.setEnabled(false);
                        btSM9.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM9.setVisibility(View.GONE);
                        btSM9.setEnabled(false);
                        medidor = mMedidoresCom8;
                        break;
                    case 21:
                        // SB9
                        btSB9.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSB9.setEnabled(true);
                        btSB9.setVisibility(View.VISIBLE);
                        btSM9.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM9.setVisibility(View.GONE);
                        btSM9.setEnabled(false);
                        btSM10.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM10.setVisibility(View.GONE);
                        btSM10.setEnabled(false);
                        medidor = mMedidoresCom9;
                        break;
                    case 22:
                        // SB10
                        btSB10.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSB10.setEnabled(true);
                        btSB10.setVisibility(View.VISIBLE);
                        btSM10.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM10.setVisibility(View.GONE);
                        btSM10.setEnabled(false);
                        btSM11.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM11.setVisibility(View.GONE);
                        btSM11.setEnabled(false);
                        medidor = mMedidoresCom10;
                        break;
                    case 23:
                        // SB11
                        btSB11.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSB11.setEnabled(true);
                        btSB11.setVisibility(View.VISIBLE);
                        btSM11.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM11.setVisibility(View.GONE);
                        btSM11.setEnabled(false);
                        btSM12.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM12.setVisibility(View.GONE);
                        btSM12.setEnabled(false);
                        medidor = mMedidoresCom11;
                        break;
                    case 24:
                        // ST1
                        btST1.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btST1.setEnabled(true);
                        btST1.setVisibility(View.VISIBLE);
                        btSM1.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM1.setVisibility(View.GONE);
                        btSM1.setEnabled(false);
                        btSM2.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM2.setVisibility(View.GONE);
                        btSM2.setEnabled(false);
                        btSM3.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM3.setVisibility(View.GONE);
                        btSM3.setEnabled(false);
                        medidor = mMedidoresCom1;
                        break;
                    case 25:
                        // ST2
                        btST2.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btST2.setEnabled(true);
                        btST2.setVisibility(View.VISIBLE);
                        btSM2.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM2.setVisibility(View.GONE);
                        btSM2.setEnabled(false);
                        btSM3.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM3.setVisibility(View.GONE);
                        btSM3.setEnabled(false);
                        btSM4.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM4.setVisibility(View.GONE);
                        btSM4.setEnabled(false);
                        medidor = mMedidoresCom2;
                        break;
                    case 26:
                        // ST3
                        btST3.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btST3.setEnabled(true);
                        btST3.setVisibility(View.VISIBLE);
                        btSM3.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM3.setVisibility(View.GONE);
                        btSM3.setEnabled(false);
                        btSM4.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM4.setVisibility(View.GONE);
                        btSM4.setEnabled(false);
                        btSM5.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM5.setVisibility(View.GONE);
                        btSM5.setEnabled(false);
                        medidor = mMedidoresCom3;
                        break;
                    case 27:
                        // ST4
                        btST4.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btST4.setEnabled(true);
                        btST4.setVisibility(View.VISIBLE);
                        btSM4.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM4.setVisibility(View.GONE);
                        btSM4.setEnabled(false);
                        btSM5.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM5.setVisibility(View.GONE);
                        btSM5.setEnabled(false);
                        btSM6.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM6.setVisibility(View.GONE);
                        btSM6.setEnabled(false);
                        medidor = mMedidoresCom4;
                        break;
                    case 28:
                        // ST5
                        btST5.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btST5.setEnabled(true);
                        btST5.setVisibility(View.VISIBLE);
                        btSM5.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM5.setVisibility(View.GONE);
                        btSM5.setEnabled(false);
                        btSM6.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM6.setVisibility(View.GONE);
                        btSM6.setEnabled(false);
                        btSM7.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM7.setVisibility(View.GONE);
                        btSM7.setEnabled(false);
                        medidor = mMedidoresCom5;
                        break;
                    case 29:
                        // ST6
                        btST6.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btST6.setEnabled(true);
                        btST6.setVisibility(View.VISIBLE);
                        btSM6.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM6.setVisibility(View.GONE);
                        btSM6.setEnabled(false);
                        btSM7.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM7.setVisibility(View.GONE);
                        btSM7.setEnabled(false);
                        btSM8.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM8.setVisibility(View.GONE);
                        btSM8.setEnabled(false);
                        medidor = mMedidoresCom6;
                        break;
                    case 30:
                        // ST7
                        btST7.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btST7.setEnabled(true);
                        btST7.setVisibility(View.VISIBLE);
                        btSM7.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM7.setVisibility(View.GONE);
                        btSM7.setEnabled(false);
                        btSM8.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM8.setVisibility(View.GONE);
                        btSM8.setEnabled(false);
                        btSM9.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM9.setVisibility(View.GONE);
                        btSM9.setEnabled(false);
                        medidor = mMedidoresCom7;
                        break;
                    case 31:
                        // ST8
                        btST8.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btST8.setEnabled(true);
                        btST8.setVisibility(View.VISIBLE);
                        btSM8.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM8.setVisibility(View.GONE);
                        btSM8.setEnabled(false);
                        btSM9.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM9.setVisibility(View.GONE);
                        btSM9.setEnabled(false);
                        btSM10.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM10.setVisibility(View.GONE);
                        btSM10.setEnabled(false);
                        medidor = mMedidoresCom8;
                        break;
                    case 32:
                        // ST9
                        btST9.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btST9.setEnabled(true);
                        btST9.setVisibility(View.VISIBLE);
                        btSM9.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM9.setVisibility(View.GONE);
                        btSM9.setEnabled(false);
                        btSM10.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM10.setVisibility(View.GONE);
                        btSM10.setEnabled(false);
                        btSM11.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM11.setVisibility(View.GONE);
                        btSM11.setEnabled(false);
                        medidor = mMedidoresCom9;
                        break;
                    case 33:
                        // ST10
                        btST10.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btST10.setEnabled(true);
                        btST10.setVisibility(View.VISIBLE);
                        btSM10.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM10.setVisibility(View.GONE);
                        btSM10.setEnabled(false);
                        btSM11.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM11.setVisibility(View.GONE);
                        btSM11.setEnabled(false);
                        btSM12.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        btSM12.setVisibility(View.GONE);
                        btSM12.setEnabled(false);
                        medidor = mMedidoresCom10;
                        break;
                }

                MedidorAbsoluto absoluto = new MedidorAbsoluto();
                absoluto.unidadeConsumidora = UCLida;
                absoluto.status = 0x99;
                if((IndexMedidorAtivar >= 1) && (IndexMedidorAtivar < 13)){
                    // Medidor Monofásico
                    absoluto.fases = 1;
                }
                else if((IndexMedidorAtivar >= 13) && (IndexMedidorAtivar < 24)){
                    // Medidor Bifásico
                    absoluto.fases = 2;
                }
                else if((IndexMedidorAtivar >= 24) && (IndexMedidorAtivar < 34)){
                    absoluto.fases = 3;
                }
                else{
                    absoluto.fases = 0;
                }
                absoluto.tipo = IndexMedidorAtivar;
                absoluto.numero = medidor;
                absoluto.numMedidor = Integer.valueOf(absoluto.numero);
                medidores.add(absoluto);

                if(medidores.size() > 2){
                    Collections.sort(medidores, new Comparator<MedidorAbsoluto>() {
                        public int compare(MedidorAbsoluto m1, MedidorAbsoluto m2) {
                            return (int) (m1.numMedidor - m2.numMedidor);
                        }
                    });
                }

                IndexMedidorAtivar = 0;

                // EnviaAB04 (Ativacao Mostradores)
                CodMostradores[0] = 0x04;
                CodMostradores[1] = 0x00;
                CodMostradores[2] = 0x00;
                CodMostradores[3] = 0x00;
                CodMostradores[4] = 0x04;
                CodMostradores[5] = 0x00;
                CodMostradores[6] = 0x00;
                CodMostradores[7] = 0x00;
                CodMostradores[8] = 0x00;
                funcaoEmExecucao = TipoOperacao.CodMostradores;
                // Deve em primeiro lugar enviar comando de leitura de parametros para remontar a lista de medidores.
                // Depois que tiver remontado pegando o numero dos novos medidores instalados
                // Aí sim pode mandar o comando AB04 para ativar os códigos dos mostradores.
                // No processamento do comando AB02 (Leitura de Parametros) deve enviar o comando AB04
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                enviaComandoAB04(medidor, (byte) 1, CodMostradores, mMetodoNumeroSerial);
            }
        }

        return false;
    }

    private Boolean processaAB04(RespostaAbsoluto respostaAbsoluto){
        ComandoAbsoluto.AB04 mAB04 = new ComandoAbsoluto.AB04();

        RespostaAbsoluto.LeituraAB04 mLeitura = respostaAbsoluto.interpretaAB04();

        Log.i("pcAB04", mLeitura.toString());

        if(mLeitura.TipoComando == 0x01){
            // Resposta de uma alteração
            Toast.makeText(this, "Código de Mostradores alterados com sucesso.", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    private void processarRespostaAberturaSessao(byte codigoResposta, RespostaAbsoluto
            respostaAbsoluto) {

        String numeroMedidor = "";
        int iUC = 0;
        int iIU = 0;

        if(mMetodoNumeroSerial == 0x02){
            numeroMedidor = respostaAbsoluto.getNumeroMedidor();
        }
        else{
            numeroMedidor = respostaAbsoluto.getSufixoNumeroMedidor();
        }
        Log.d(TAG, "Abertura de sessão para o medidor: " + numeroMedidor + "\n" + respostaAbsoluto.toString());

        if (codigoResposta == 0x01) {
            Toast.makeText(ConfiguracaoMedidor.this, getString(R.string.text_senha_expira),
                    Toast.LENGTH_SHORT).show();
        } else if (codigoResposta == 0x02) {
            showErrorMessage(getString(R.string.text_senha_expirada));
            return;
        } else if (codigoResposta == 0x03) {
            showErrorMessage(getString(R.string.text_medidor_protecao));
            return;
        } else if (codigoResposta == 0x04) {
            showErrorMessage(getString(R.string.text_senha_invalida));
            return;
        }

        bSenhaDigitada = true;

            if((TipoOperacao.Ativacao == funcaoEmExecucao)||(TipoOperacao.Desativacao == funcaoEmExecucao))
            {
                boolean bLocalizouMedidor = false;
                for(int x=0;x<medidores.size();x++)
                {
                    String medidor = medidores.get(x).numero;
                    if(medidor.trim().equals(numeroMedidor))
                    {
                        iUC = Integer.parseInt(medidores.get(x).unidadeConsumidora);
                        bLocalizouMedidor = true;
                        break;
                    }
                }

                if(bLocalizouMedidor)
                {
                    String UCAux = etUC.getText().toString();
                    byte Ativacao = 0;
                    if(TipoOperacao.Desativacao == funcaoEmExecucao)
                    {
                        Ativacao = 0;
                    }
                    else if(TipoOperacao.Ativacao == funcaoEmExecucao)
                    {
                        Ativacao = 1;
                        for(int y=0; y < medidores.size(); y++){
                            String MedidorLido = medidores.get(y).unidadeConsumidora;
                            if(MedidorLido.length() > 8){
                                MedidorLido = MedidorLido.substring(MedidorLido.length()-8, MedidorLido.length());
                            }
                            if(MedidorLido.trim().equals(UCAux.trim())){
                                // Medidor já utilizado
                                Toast.makeText(this, "UC digitada já pertence a outro medidor.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                    if(UCAux.trim().equals("".trim())){
                        UCAux = "00000000";
                    }
                    iUC = Integer.parseInt(UCAux);
                    String IUAux = etIU.getText().toString();
                    if(IUAux.trim().equals("".trim())){
                        IUAux = "00000000";
                    }
                    iIU = Integer.parseInt(IUAux);
                    enviaComandoAB03(numeroMedidor, IndexMedidorAtivar, (byte) 1, Ativacao, iUC, iIU, 0, mMetodoNumeroSerial);
                    //Toast.makeText(this, "Enviou cmd ativação = " + Byte.valueOf(Ativacao).toString(), Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "Não Localizou medidor na lista.", Toast.LENGTH_SHORT).show();
                }
            }
            else if(TipoOperacao.CodMostradores == funcaoEmExecucao){
            }
    }

    private void processarRespostaGrandezasInstantaneas(RespostaAbsoluto resposta) {
        g = resposta.interpretarGrandezasInstantaneas();
        Locale br = Locale.forLanguageTag("BR");

        g.VAB = calcularTensaoDeLinha(g.VA, g.anguloVA, g.VB, g.anguloVB);
        g.VBC = calcularTensaoDeLinha(g.VB, g.anguloVB, g.VC, g.anguloVC);
        g.VCA = calcularTensaoDeLinha(g.VC, g.anguloVC, g.VA, g.anguloVA);

        g.cosphiA = Math.abs((float) Math.cos(g.defasagemIA * Math.PI / 180));
        g.cosphiB = Math.abs((float) Math.cos(g.defasagemIB * Math.PI / 180));
        g.cosphiC = Math.abs((float) Math.cos(g.defasagemIC * Math.PI / 180));
        g.SA = Math.abs(g.PA / g.cosphiA);
        g.SB = Math.abs(g.PB / g.cosphiA);
        g.SC = Math.abs(g.PC / g.cosphiA);
        g.ST = Math.abs((float) Math.sqrt(g.PT * g.PT + g.QT * g.QT));
        if (g.ST > 0)
            g.cosphiT = Math.abs(g.PT) / g.ST;
        else
            g.cosphiT = 1;

        g.FPA = 100 * g.cosphiA;
        g.FPB = 100 * g.cosphiB;
        g.FPC = 100 * g.cosphiC;
        g.FPT = 100 * g.cosphiT;
        mTemperatura.setText(String.format(br, getString(R.string.temperatura), g.temperatura));
        mVersao.setText(getString(R.string.text_versao, mVersaoMedidor));
        mEstadoTampa.setText(getString(R.string.text_estado_tampa) + g.EstadoTampa);
        mHorarioLocal.setText(String.format(getString(R.string.horario_local),
                new SimpleDateFormat("dd/MM/yyyy' 'HH:mm:ss").format(Calendar.getInstance().getTime())));
        mHorarioMedidor.setText(String.format(getString(R.string.horario_medidor), g.horario));
        mVa.setText(String.format(br, getResources().getString(R.string.formata_tensao), formataValor(g.VA)));
        mVb.setText(String.format(br, getResources().getString(R.string.formata_tensao), formataValor(g.VB)));
        mVc.setText(String.format(br, getResources().getString(R.string.formata_tensao), formataValor(g.VC)));
        mVab.setText(String.format(br, getResources().getString(R.string.formata_tensao), formataValor(g.VAB)));
        mVbc.setText(String.format(br, getResources().getString(R.string.formata_tensao), formataValor(g.VBC)));
        mVca.setText(String.format(br, getResources().getString(R.string.formata_tensao), formataValor(g.VCA)));
        mIa.setText(String.format(br, getResources().getString(R.string.formata_corrente), formataValor(g.IA)));
        mIb.setText(String.format(br, getResources().getString(R.string.formata_corrente), formataValor(g.IB)));
        mIc.setText(String.format(br, getResources().getString(R.string.formata_corrente), formataValor(g.IC)));
        mAnguloVa.setText(String.format(br, getResources().getString(R.string.formata_angulo), g.anguloVA));
        mAnguloVb.setText(String.format(br, getResources().getString(R.string.formata_angulo), g.anguloVB));
        mAnguloVc.setText(String.format(br, getResources().getString(R.string.formata_angulo), g.anguloVC));
        mDefasagemIa.setText(String.format(br, getResources().getString(R.string.formata_angulo), g.defasagemIA));
        mDefasagemIb.setText(String.format(br, getResources().getString(R.string.formata_angulo), g.defasagemIB));
        mDefasagemIc.setText(String.format(br, getResources().getString(R.string.formata_angulo), g.defasagemIC));
        mFrequencia.setText(String.format(br, getResources().getString(R.string.formata_frequencia), g.frequencia));
        mPa.setText(String.format(br, getString(R.string.formata_potencia_ativa), formataValor(g.PA)));
        mPb.setText(String.format(br, getString(R.string.formata_potencia_ativa), formataValor(g.PB)));
        mPc.setText(String.format(br, getString(R.string.formata_potencia_ativa), formataValor(g.PC)));
        mPt.setText(String.format(br, getString(R.string.formata_potencia_ativa), formataValor(g.PT)));
        mQa.setText(String.format(br, getString(R.string.formata_potencia_reativa), formataValor(g.QA)));
        mQb.setText(String.format(br, getString(R.string.formata_potencia_reativa), formataValor(g.QB)));
        mQc.setText(String.format(br, getString(R.string.formata_potencia_reativa), formataValor(g.QC)));
        mQt.setText(String.format(br, getString(R.string.formata_potencia_reativa), formataValor(g.QT)));
        mSa.setText(String.format(br, getString(R.string.formata_potencia_aparente), formataValor(g.SA)));
        mSb.setText(String.format(br, getString(R.string.formata_potencia_aparente), formataValor(g.SB)));
        mSc.setText(String.format(br, getString(R.string.formata_potencia_aparente), formataValor(g.SC)));
        mSt.setText(String.format(br, getString(R.string.formata_potencia_aparente), formataValor(g.ST)));
        mFPa.setText(String.format(br, getString(R.string.formata_fator_potencia), g.FPA));
        mFPb.setText(String.format(br, getString(R.string.formata_fator_potencia), g.FPB));
        mFPc.setText(String.format(br, getString(R.string.formata_fator_potencia), g.FPC));
        mFPt.setText(String.format(br, getString(R.string.formata_fator_potencia), g.FPT));
        mCFa.setText(String.format(br, getString(R.string.formata_cosseno_fi), g.cosphiA));
        mCFb.setText(String.format(br, getString(R.string.formata_cosseno_fi), g.cosphiB));
        mCFc.setText(String.format(br, getString(R.string.formata_cosseno_fi), g.cosphiC));
        mCFt.setText(String.format(br, getString(R.string.formata_cosseno_fi), g.cosphiT));
        mDhtA.setText(String.format(br, getString(R.string.formata_dht), g.DHT_A));
        mDhtB.setText(String.format(br, getString(R.string.formata_dht), g.DHT_B));
        mDhtC.setText(String.format(br, getString(R.string.formata_dht), g.DHT_C));
        mDhcA.setText(String.format(br, getString(R.string.formata_dht), g.DHC_A));
        mDhcB.setText(String.format(br, getString(R.string.formata_dht), g.DHC_B));
        mDhcC.setText(String.format(br, getString(R.string.formata_dht), g.DHC_C));

        float maxV = Math.max(g.VA, Math.max(g.VB, g.VC)) / (float) 0.9;
        if (maxV == 0)
            maxV = 1;
        float maxI = Math.max(g.IA, Math.max(g.IB, g.IC)) / (float) 0.6;
        if (maxI == 0)
            maxI = 1;

        List<Fasor> fasores = new ArrayList<>();
        fasores.add(new Fasor(g.VA/maxV, g.anguloVA, 0xffff0000, "VA", Fasor.HeadType.ARROW));
        fasores.add(new Fasor(g.VB/maxV, g.anguloVB, 0xff0000ff, "VB", Fasor.HeadType.ARROW));
        fasores.add(new Fasor(g.VC/maxV, g.anguloVC, 0xff00ff00, "VC", Fasor.HeadType.ARROW));
        fasores.add(new Fasor(g.IA /maxI, g.defasagemIA + g.anguloVA, 0xffff007f, "IA", Fasor.HeadType.ARROW));
        fasores.add(new Fasor(g.IB /maxI, g.defasagemIB + g.anguloVB, 0xff007fff, "IB", Fasor.HeadType.ARROW));
        fasores.add(new Fasor(g.IC /maxI, g.defasagemIC + g.anguloVC, 0xff7fff00, "IC", Fasor.HeadType.ARROW));
        mDiagramaFasorial.setFasors(fasores);
        mNumeroMedidorArquivo = resposta.getNumeroMedidor();
    }

    private String formataValor(float valor) {
        Locale br = Locale.forLanguageTag("BR");
        String sufixo = "";
        String resultado;

        float v = Math.abs(valor);
        if (v > 1000000000) {
            valor /= 1000000000;
            sufixo = "G";
        } else if (v > 1000000) {
            valor /= 1000000;
            sufixo = "M";
        } else if (valor > 1000){
            valor /= 1000;
            sufixo = "k";
        }

        v = Math.abs(valor);
        if (v < 10)
            resultado = String.format(br, "%.3f %s", valor, sufixo);
        else if (v < 100)
            resultado = String.format(br, "%.2f %s", valor, sufixo);
        else if (v < 1000)
            resultado = String.format(br, "%.1f %s", valor, sufixo);
        else
            resultado = String.format(br, "%.0f %s", valor, sufixo);
        return resultado;
    }

    private float calcularTensaoDeLinha(float v1, float anguloV1, float v2, float anguloV2) {
        float anguloVAB = converterGrausParaRadianos(corrigirAngulo(anguloV1 - anguloV2));
        return (float) Math.sqrt(Math.abs(((v1 * v1) + (v2 * v2))
                - 2 * v1 * v2 * Math.cos(anguloVAB)));
    }

    private float corrigirAngulo(float angulo) {
        if (angulo > 180)
            return angulo - 360;
        if (angulo < -180)
            return angulo + 360;
        return angulo;
    }

    private float converterGrausParaRadianos(float anguloGraus) {
        return (float) (anguloGraus * Math.PI / 180);
    }

    private void salvarArquivo() {
        Date date = Calendar.getInstance().getTime();
        String nomeArquivo = String.format("PaginaFiscal_%s_%s_%s.csv",  mNumeroMedidorArquivo, mDeviceName.substring(4).trim(),
                new SimpleDateFormat("ddMMyyyy'_'HHmmss").format(date));
        if (!Arquivo.salvarArquivo(ConfiguracaoMedidor.this, nomeArquivo, getDadosArquivo())) {
            Toast.makeText(ConfiguracaoMedidor.this, "Erro ao salvar arquivo",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ConfiguracaoMedidor.this, "Arquivo salvo com sucesso:\n" + nomeArquivo,
                    Toast.LENGTH_LONG).show();
        }
    }

    private String getDadosArquivo() {
        DecimalFormat f = new DecimalFormat("0000000000");
        Date date = Calendar.getInstance().getTime();
        mDataMedidor = g.horario;
        return
                "Medidor;" + mNumeroMedidor + "\n" +
                        "Data/Hora Medidor;" + mDataMedidor + "\n" +
                        "Data/Hora Local;" + new SimpleDateFormat("dd/MM/yyyy' 'HH:mm:ss").format(date) + "\n" +
                        "VA;" + mVa.getText() + "\n" +
                        "VB;" + mVb.getText() + "\n" +
                        "VC;" + mVc.getText() + "\n" +
                        "VAB;" + mVab.getText() + "\n" +
                        "VBC;" + mVbc.getText() + "\n" +
                        "VCA;" + mVca.getText() + "\n" +
                        "Angulo Fase A;" + mAnguloVa.getText() + "\n" +
                        "Angulo Fase B;" + mAnguloVb.getText() + "\n" +
                        "Angulo Fase C;" + mAnguloVc.getText() + "\n" +
                        "IA;" + mIa.getText() + "\n" +
                        "IB;" + mIb.getText() + "\n" +
                        "IC;" + mIc.getText() + "\n" +
                        "Defasagem A;" + mDefasagemIa.getText() + "\n" +
                        "Defasagem B;" + mDefasagemIb.getText() + "\n" +
                        "Defasagem C;" + mDefasagemIc.getText() + "\n" +
                        "Frequencia;" + mFrequencia.getText() + "\n" +
                        "Potencia Ativa A;" + mPa.getText() + "\n" +
                        "Potencia Ativa B;" + mPb.getText() + "\n" +
                        "Potencia Ativa C;" + mPc.getText() + "\n" +
                        "Potencia Ativa Trifasico;" + mPt.getText() + "\n" +
                        "Potencia Reativa A;" + mQa.getText() + "\n" +
                        "Potencia Reativa B;" + mQb.getText() + "\n" +
                        "Potencia Reativa C;" + mQc.getText() + "\n" +
                        "Potencia Reativa Trifasico;" + mQt.getText() + "\n" +
                        "Potencia Aparente A;" + mSa.getText() + "\n" +
                        "Potencia Aparente B;" + mSb.getText() + "\n" +
                        "Potencia Aparente C;" + mSc.getText() + "\n" +
                        "Potencia Aparente Trifasico;" + mSt.getText() + "\n" +
                        "Fator de Potencia A;" + mFPa.getText() + "\n" +
                        "Fator de Potencia B;" + mFPb.getText() + "\n" +
                        "Fator de Potencia C;" + mFPc.getText() + "\n" +
                        "Fator de Potencia Trifasico;" + mFPt.getText() + "\n" +
                        "Coeficiente Fi A;" + mCFa.getText() + "\n" +
                        "Coeficiente Fi B;" + mCFb.getText() + "\n" +
                        "Coeficiente Fi C;" + mCFc.getText() + "\n" +
                        "Coeficiente Fi Trifasico;" + mCFt.getText() + "\n" +
                        "Temperatura;" + String.format(Locale.forLanguageTag("BR"),"%.1fº", g.temperatura) + "\n";
    }

    private void PreparaTela(long mMedidoresAtivos)
    {
        //------------------------------------------------------------
        // Inicio da configuração das saídas monofásicas
        //------------------------------------------------------------
        if(((mMedidoresAtivos >> 1) & 0x01) == 0x01)
        {
            // Medidor 1 ativo
            // bit 0 ligado indicando medidor monofásico SM1 ativo
            MedidoresMonofasicosDisponiveis &= 0xFFFE;
            btSM1.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSM1.setVisibility(View.VISIBLE);
            btSM1.setEnabled(true);

            // bifásico SB1 não pode ser utilizado
            MedidoresBifasicosDisponiveis &= 0xFFFE;
            btSB1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
            btSB1.setEnabled(false);
            btSB1.setVisibility(View.GONE);

            // trifásico ST1 não pode ser utilizado
            MedidoresTrifasicosDisponiveis &= 0xFFFE;
            btST1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
            btST1.setEnabled(false);
            btST1.setVisibility(View.GONE);

            // Hexafásico SH1 não pode ser utilizado
            MedidoresHexafasicosDisponiveis &= 0xFFFE;
            btSH1.setVisibility(View.GONE);
            btSH1.setEnabled(false);
            btSH1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }
        if(((mMedidoresAtivos >> 2) & 0x01) == 0x01)
        {
            // Medidor 2 ativo
            // bit 1 ligado indicando medidor monofásico SM2 ativo
            MedidoresMonofasicosDisponiveis &= 0xFFFD;
            btSM2.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSM2.setEnabled(true);
            btSM2.setVisibility(View.VISIBLE);

            // bifásico SB1 e SB2 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xFFFC;
            btSB1.setVisibility(View.GONE);
            btSB1.setEnabled(false);
            btSB1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            //clGlobal.Medidores2[14].bAtivo = false;
            btSB2.setVisibility(View.GONE);
            btSB2.setEnabled(false);
            btSB2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST1 e ST2 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFFFC;
            btST1.setVisibility(View.GONE);
            btST1.setEnabled(false);
            btST1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            //clGlobal.Medidores2[25].bAtivo = false;
            btST2.setVisibility(View.GONE);
            btST2.setEnabled(false);
            btST2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // Hexafásico SH1 não pode ser utilizado
            MedidoresHexafasicosDisponiveis &= 0xFFFE;
            btSH1.setVisibility(View.GONE);
            btSH1.setEnabled(false);
            btSH1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }
        if(((mMedidoresAtivos >> 3) & 0x01) == 0x01)
        {
            // Medidor 3 ativo
            // bit 2 ligado indicando medidor monofásico SM3 ativo
            MedidoresMonofasicosDisponiveis &= 0xFFFB;
            btSM3.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSM3.setVisibility(View.VISIBLE);
            btSM3.setEnabled(true);

            // bifásico SB2 e SB3 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xFFF9;
            btSB2.setVisibility(View.GONE);
            btSB2.setEnabled(false);
            btSB2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            //clGlobal.Medidores2[15].bAtivo = false;
            btSB3.setVisibility(View.GONE);
            btSB3.setEnabled(false);
            btSB3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST1, ST2 e ST3 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFFF8;
            btST1.setVisibility(View.GONE);
            btST1.setEnabled(false);
            btST1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST2.setVisibility(View.GONE);
            btST2.setEnabled(false);
            btST2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST3.setVisibility(View.GONE);
            btST3.setEnabled(false);
            btST3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // Hexafásico SH1 não pode ser utilizado
            MedidoresHexafasicosDisponiveis &= 0xFFFE;
            btSH1.setVisibility(View.GONE);
            btSH1.setEnabled(false);
            btSH1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }
        if(((mMedidoresAtivos >> 4) & 0x01) == 0x01)
        {
            // Medidor 4 ativo
            // bit 3 ligado indicando medidor monofásico SM4 ativo
            MedidoresMonofasicosDisponiveis &= 0xFFF7;
            btSM4.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSM4.setVisibility(View.VISIBLE);
            btSM4.setEnabled(true);

            // bifásico SB3 e SB4 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xFFF3;
            btSB3.setVisibility(View.GONE);
            btSB3.setEnabled(false);
            btSB3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB4.setVisibility(View.GONE);
            btSB4.setEnabled(false);
            btSB4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST2, ST3 e ST4 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFFF1;
            btST2.setVisibility(View.GONE);
            btST2.setEnabled(false);
            btST2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST3.setVisibility(View.GONE);
            btST3.setEnabled(false);
            btST3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST4.setVisibility(View.GONE);
            btST4.setEnabled(false);
            btST4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // Hexafásico SH1 não pode ser utilizado
            MedidoresHexafasicosDisponiveis &= 0xFFFE;
            btSH1.setVisibility(View.GONE);
            btSH1.setEnabled(false);
            btSH1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }
        if(((mMedidoresAtivos >> 5) & 0x01) == 0x01)
        {
            // Medidor 5 ativo
            // bit 4 ligado indicando medidor monofásico SM5 ativo
            MedidoresMonofasicosDisponiveis &= 0xFFEF;
            btSM5.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSM5.setVisibility(View.VISIBLE);
            btSM5.setEnabled(true);

            // bifásico SB4 e SB5 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xFFE7;
            btSB4.setVisibility(View.GONE);
            btSB4.setEnabled(false);
            btSB4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB5.setVisibility(View.GONE);
            btSB5.setEnabled(false);
            btSB5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST3, ST4 e ST5 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFFE3;
            btST3.setVisibility(View.GONE);
            btST3.setEnabled(false);
            btST3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST4.setVisibility(View.GONE);
            btST4.setEnabled(false);
            btST4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST5.setVisibility(View.GONE);
            btST5.setEnabled(false);
            btST5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // Hexafásico SH1 não pode ser utilizado
            MedidoresHexafasicosDisponiveis &= 0xFFFE;
            btSH1.setVisibility(View.GONE);
            btSH1.setEnabled(false);
            btSH1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }
        if(((mMedidoresAtivos >> 6) & 0x01) == 0x01)
        {
            // Medidor 6 ativo
            // bit 5 ligado indicando medidor monofásico SM6 ativo
            MedidoresMonofasicosDisponiveis &= 0xFFDF;
            btSM6.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSM6.setVisibility(View.VISIBLE);
            btSM6.setEnabled(true);

            // bifásico SB5 e SB6 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xFFCF;
            btSB5.setVisibility(View.GONE);
            btSB5.setEnabled(false);
            btSB5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB6.setVisibility(View.GONE);
            btSB6.setEnabled(false);
            btSB6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST4, ST5 e ST6 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFFC7;
            btST4.setVisibility(View.GONE);
            btST4.setEnabled(false);
            btST4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST5.setVisibility(View.GONE);
            btST5.setEnabled(false);
            btST5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST6.setVisibility(View.GONE);
            btST6.setEnabled(false);
            btST6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // Hexafásico SH1 não pode ser utilizado
            MedidoresHexafasicosDisponiveis &= 0xFFFE;
            btSH1.setVisibility(View.GONE);
            btSH1.setEnabled(false);
            btSH1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }
        if(((mMedidoresAtivos >> 7) & 0x01) == 0x01)
        {
            // Medidor 7 ativo
            // bit 6 ligado indicando medidor monofásico SM7 ativo
            MedidoresMonofasicosDisponiveis &= 0xFFBF;
            btSM7.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSM7.setVisibility(View.VISIBLE);
            btSM7.setEnabled(true);

            // bifásico SB6 e SB7 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xFF9F;
            btSB6.setVisibility(View.GONE);
            btSB6.setEnabled(false);
            btSB6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB7.setVisibility(View.GONE);
            btSB7.setEnabled(false);
            btSB7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST5, ST6 e ST7 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFF8F;
            btST5.setVisibility(View.GONE);
            btST5.setEnabled(false);
            btST5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST6.setVisibility(View.GONE);
            btST6.setEnabled(false);
            btST6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST7.setVisibility(View.GONE);
            btST7.setEnabled(false);
            btST7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }
        if(((mMedidoresAtivos >> 8) & 0x01) == 0x01)
        {
            // Medidor 8 ativo
            // bit 7 ligado indicando medidor monofásico SM8 ativo
            MedidoresMonofasicosDisponiveis &= 0xFF7F;
            btSM8.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSM8.setVisibility(View.VISIBLE);
            btSM8.setEnabled(true);

            // bifásico SB7 e SB8 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xFF3F;
            btSB7.setVisibility(View.GONE);
            btSB7.setEnabled(false);
            btSB7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB8.setVisibility(View.GONE);
            btSB8.setEnabled(false);
            btSB8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST6, ST7 e ST8 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFF1F;
            btST6.setVisibility(View.GONE);
            btST6.setEnabled(false);
            btST6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST7.setVisibility(View.GONE);
            btST7.setEnabled(false);
            btST7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST8.setVisibility(View.GONE);
            btST8.setEnabled(false);
            btST8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }
        if(((mMedidoresAtivos >> 9) & 0x01) == 0x01)
        {
            // Medidor 9 ativo
            // bit 0 ligado indicando medidor monofásico SM9 ativo
            MedidoresMonofasicosDisponiveis &= 0xFEFF;
            btSM9.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSM9.setVisibility(View.VISIBLE);
            btSM9.setEnabled(true);

            // bifásico SB8 e SB9 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xFE7F;
            btSB8.setVisibility(View.GONE);
            btSB8.setEnabled(false);
            btSB8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB9.setVisibility(View.GONE);
            btSB9.setEnabled(false);
            btSB9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST7, ST8 e ST9 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFE3F;
            btST7.setVisibility(View.GONE);
            btST7.setEnabled(false);
            btST7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST8.setVisibility(View.GONE);
            btST8.setEnabled(false);
            btST8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST9.setVisibility(View.GONE);
            btST9.setEnabled(false);
            btST9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }
        if(((mMedidoresAtivos >> 10) & 0x01) == 0x01)
        {
            // Medidor 10 ativo
            // bit 1 ligado indicando medidor monofásico SM10 ativo
            MedidoresMonofasicosDisponiveis &= 0xFDFF;
            btSM10.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSM10.setVisibility(View.VISIBLE);
            btSM10.setEnabled(true);

            // bifásico SB9 e SB10 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xFCFF;
            btSB9.setVisibility(View.GONE);
            btSB9.setEnabled(false);
            btSB9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB10.setVisibility(View.GONE);
            btSB10.setEnabled(false);
            btSB10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST8, ST9 e ST10 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFC7F;
            btST8.setVisibility(View.GONE);
            btST8.setEnabled(false);
            btST8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST9.setVisibility(View.GONE);
            btST9.setEnabled(false);
            btST9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST10.setVisibility(View.GONE);
            btST10.setEnabled(false);
            btST10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }
        if(((mMedidoresAtivos >> 11) & 0x01) == 0x01)
        {
            // Medidor 11 ativo
            // bit 2 ligado indicando medidor monofásico SM11 ativo
            MedidoresMonofasicosDisponiveis &= 0xFBFF;
            btSM11.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSM11.setVisibility(View.VISIBLE);
            btSM11.setEnabled(true);

            // bifásico SB10 e SB11 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xF9FF;
            btSB10.setVisibility(View.GONE);
            btSB10.setEnabled(false);
            btSB10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB11.setVisibility(View.GONE);
            btSB11.setEnabled(false);
            btSB11.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST9 e ST10 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFCFF;
            btST9.setVisibility(View.GONE);
            btST9.setEnabled(false);
            btST9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST10.setVisibility(View.GONE);
            btST10.setEnabled(false);
            btST10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }
        if(((mMedidoresAtivos >> 12) & 0x01) == 0x01)
        {
            // Medidor 12 ativo
            // bit 3 ligado indicando medidor monofásico SM12 ativo
            MedidoresMonofasicosDisponiveis &= 0xF7FF;
            btSM12.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSM12.setVisibility(View.VISIBLE);
            btSM12.setEnabled(true);

            // bifásico SB11 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xFBFF;
            btSB11.setVisibility(View.GONE);
            btSB11.setEnabled(false);
            btSB11.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST10 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFDFF;
            btST10.setVisibility(View.GONE);
            btST10.setEnabled(false);
            btST10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }
        //------------------------------------------------------------
        // Fim da configuração das saídas monofásicas
        //------------------------------------------------------------

        //------------------------------------------------------------
        // Inicio da configuração das saídas bifásicas
        //------------------------------------------------------------
        if (((mMedidoresAtivos >> 13) & 0x01) == 0x01)
        {
            // Medidor 13 ativo
            // bit 12 ligado indicando medidor Bifásico SB1 ativo
            MedidoresBifasicosDisponiveis &= 0xFFFE; // indicando SB1 indisponível
            btSB1.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSB1.setVisibility(View.VISIBLE);
            btSB1.setEnabled(true);

            // monofásico SM1 e SM2 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xFFFC;
            btSM1.setVisibility(View.GONE);
            btSM1.setEnabled(false);
            btSM1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM2.setVisibility(View.GONE);
            btSM2.setEnabled(false);
            btSM2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB2 não pode ser utilizado
            MedidoresBifasicosDisponiveis &= 0xFFFD;
            btSB2.setVisibility(View.GONE);
            btSB2.setEnabled(false);
            btSB2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST1 e ST2 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFFFC;
            btST1.setVisibility(View.GONE);
            btST1.setEnabled(false);
            btST1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST2.setVisibility(View.GONE);
            btST2.setEnabled(false);
            btST2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // Hexafásico SH1 não pode ser utilizado
            MedidoresHexafasicosDisponiveis &= 0xFFFE;
            btSH1.setVisibility(View.GONE);
            btSH1.setEnabled(false);
            btSH1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        if (((mMedidoresAtivos >> 14) & 0x01) == 0x01)
        {
            // Medidor 14 ativo
            // bit 13 ligado indicando medidor Bifásico SB2 ativo
            MedidoresBifasicosDisponiveis &= 0xFFFD; // indicando SB2 indisponível
            btSB2.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSB2.setVisibility(View.VISIBLE);
            btSB2.setEnabled(true);

            // monofásico SM2 e SM3 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xFFF9;
            btSM2.setVisibility(View.GONE);
            btSM2.setEnabled(false);
            btSM2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM3.setVisibility(View.GONE);
            btSM3.setEnabled(false);
            btSM3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB1 e SB3 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xFFFA;
            btSB1.setVisibility(View.GONE);
            btSB1.setEnabled(false);
            btSB1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB3.setVisibility(View.GONE);
            btSB3.setEnabled(false);
            btSB3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST1, ST2 e ST3 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFFF8;
            btST1.setVisibility(View.GONE);
            btST1.setEnabled(false);
            btST1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST2.setVisibility(View.GONE);
            btST2.setEnabled(false);
            btST2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST3.setVisibility(View.GONE);
            btST3.setEnabled(false);
            btST3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // Hexafásico SH1 não pode ser utilizado
            MedidoresHexafasicosDisponiveis &= 0xFFFE;
            btSH1.setVisibility(View.GONE);
            btSH1.setEnabled(false);
            btSH1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        if (((mMedidoresAtivos >> 15) & 0x01) == 0x01)
        {
            // Medidor 15 ativo
            // bit 14 ligado indicando medidor Bifásico SB3 ativo
            MedidoresBifasicosDisponiveis &= 0xFFFB; // indicando SB3 indisponível
            btSB3.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSB3.setVisibility(View.VISIBLE);
            btSB3.setEnabled(true);

            // monofásico SM3 e SM4 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xFFF3;
            btSM3.setVisibility(View.GONE);
            btSM3.setEnabled(false);
            btSM3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM4.setVisibility(View.GONE);
            btSM4.setEnabled(false);
            btSM4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB2 e SB4 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xFFF5;
            btSB2.setVisibility(View.GONE);
            btSB2.setEnabled(false);
            btSB2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB4.setVisibility(View.GONE);
            btSB4.setEnabled(false);
            btSB4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST1, ST2, ST3 e ST4 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFFF0;
            btST1.setVisibility(View.GONE);
            btST1.setEnabled(false);
            btST1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST2.setVisibility(View.GONE);
            btST2.setEnabled(false);
            btST2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST3.setVisibility(View.GONE);
            btST3.setEnabled(false);
            btST3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST4.setVisibility(View.GONE);
            btST4.setEnabled(false);
            btST4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // Hexafásico SH1 não pode ser utilizado
            MedidoresHexafasicosDisponiveis &= 0xFFFE;
            btSH1.setVisibility(View.GONE);
            btSH1.setEnabled(false);
            btSH1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        if (((mMedidoresAtivos >> 16) & 0x01) == 0x01)
        {
            // Medidor 16 ativo
            // bit 15 ligado indicando medidor Bifásico SB4 ativo
            MedidoresBifasicosDisponiveis &= 0xFFF7; // indicando SB4 indisponível
            btSB4.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSB4.setVisibility(View.VISIBLE);
            btSB4.setEnabled(true);

            // monofásico SM4 e SM5 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xFFE7;
            btSM4.setVisibility(View.GONE);
            btSM4.setEnabled(false);
            btSM4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM5.setVisibility(View.GONE);
            btSM5.setEnabled(false);
            btSM5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB3 e SB5 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xFFEB;
            btSB3.setVisibility(View.GONE);
            btSB3.setEnabled(false);
            btSB3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB5.setVisibility(View.GONE);
            btSB5.setEnabled(false);
            btSB5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST2, ST3, ST4 e ST5 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFFE1;
            btST2.setVisibility(View.GONE);
            btST2.setEnabled(false);
            btST2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST3.setVisibility(View.GONE);
            btST3.setEnabled(false);
            btST3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST4.setVisibility(View.GONE);
            btST4.setEnabled(false);
            btST4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST5.setVisibility(View.GONE);
            btST5.setEnabled(false);
            btST5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // Hexafásico SH1 não pode ser utilizado
            MedidoresHexafasicosDisponiveis &= 0xFFFE;
            btSH1.setVisibility(View.GONE);
            btSH1.setEnabled(false);
            btSH1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        if (((mMedidoresAtivos >> 17) & 0x01) == 0x01)
        {
            // Medidor 17 ativo
            // bit 16 ligado indicando medidor Bifásico SB5 ativo
            MedidoresBifasicosDisponiveis &= 0xFFEF; // indicando SB5 indisponível
            btSB5.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSB5.setVisibility(View.VISIBLE);
            btSB5.setEnabled(true);

            // monofásico SM5 e SM6 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xFFCF;
            btSM5.setVisibility(View.GONE);
            btSM5.setEnabled(false);
            btSM5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM6.setVisibility(View.GONE);
            btSM6.setEnabled(false);
            btSM6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB4 e SB6 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xFFD7;
            btSB4.setVisibility(View.GONE);
            btSB4.setEnabled(false);
            btSB4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB6.setVisibility(View.GONE);
            btSB6.setEnabled(false);
            btSB6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST3, ST4, ST5 e ST6 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFFC3;
            btST3.setVisibility(View.GONE);
            btST3.setEnabled(false);
            btST3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST4.setVisibility(View.GONE);
            btST4.setEnabled(false);
            btST4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST5.setVisibility(View.GONE);
            btST5.setEnabled(false);
            btST5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST6.setVisibility(View.GONE);
            btST6.setEnabled(false);
            btST6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // Hexafásico SH1 não pode ser utilizado
            MedidoresHexafasicosDisponiveis &= 0xFFFE;
            btSH1.setVisibility(View.GONE);
            btSH1.setEnabled(false);
            btSH1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        if (((mMedidoresAtivos >> 18) & 0x01) == 0x01)
        {
            // Medidor 18 ativo
            // bit 17 ligado indicando medidor Bifásico SB6 ativo
            MedidoresBifasicosDisponiveis &= 0xFFDF; // indicando SB6 indisponível
            btSB6.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSB6.setVisibility(View.VISIBLE);
            btSB6.setEnabled(true);

            // monofásico SM6 e SM7 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xFF9F;
            btSM6.setVisibility(View.GONE);
            btSM6.setEnabled(false);
            btSM6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM7.setVisibility(View.GONE);
            btSM7.setEnabled(false);
            btSM7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB5 e SB7 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xFFAF;
            btSB5.setVisibility(View.GONE);
            btSB5.setEnabled(false);
            btSB5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB7.setVisibility(View.GONE);
            btSB7.setEnabled(false);
            btSB7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST4, ST5, ST6 e ST7 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFF87;
            btST4.setVisibility(View.GONE);
            btST4.setEnabled(false);
            btST4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST5.setVisibility(View.GONE);
            btST5.setEnabled(false);
            btST5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST6.setVisibility(View.GONE);
            btST6.setEnabled(false);
            btST6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST7.setVisibility(View.GONE);
            btST7.setEnabled(false);
            btST7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // Hexafásico SH1 não pode ser utilizado
            MedidoresHexafasicosDisponiveis &= 0xFFFE;
            btSH1.setVisibility(View.GONE);
            btSH1.setEnabled(false);
            btSH1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        if (((mMedidoresAtivos >> 19) & 0x01) == 0x01)
        {
            // Medidor 19 ativo
            // bit 18 ligado indicando medidor Bifásico SB7 ativo
            MedidoresBifasicosDisponiveis &= 0xFFBF; // indicando SB7 indisponível
            btSB7.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSB7.setVisibility(View.VISIBLE);
            btSB7.setEnabled(true);

            // monofásico SM7 e SM8 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xFF3F;
            btSM7.setVisibility(View.GONE);
            btSM7.setEnabled(false);
            btSM7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM8.setVisibility(View.GONE);
            btSM8.setEnabled(false);
            btSM8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB6 e SB8 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xFF5F;
            btSB6.setVisibility(View.GONE);
            btSB6.setEnabled(false);
            btSB6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB8.setVisibility(View.GONE);
            btSB8.setEnabled(false);
            btSB8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST5, ST6, ST7 e ST8 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFF0F;
            btST5.setVisibility(View.GONE);
            btST5.setEnabled(false);
            btST5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST6.setVisibility(View.GONE);
            btST6.setEnabled(false);
            btST6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST7.setVisibility(View.GONE);
            btST7.setEnabled(false);
            btST7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST8.setVisibility(View.GONE);
            btST8.setEnabled(false);
            btST8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        if (((mMedidoresAtivos >> 20) & 0x01) == 0x01)
        {
            // Medidor 20 ativo
            // bit 19 ligado indicando medidor Bifásico SB8 ativo
            MedidoresBifasicosDisponiveis &= 0xFF7F; // indicando SB8 indisponível
            btSB8.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSB8.setVisibility(View.VISIBLE);
            btSB8.setEnabled(true);

            // monofásico SM8 e SM9 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xFE7F;
            btSM8.setVisibility(View.GONE);
            btSM8.setEnabled(false);
            btSM8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM9.setVisibility(View.GONE);
            btSM9.setEnabled(false);
            btSM9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB7 e SB9 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xFEBF;
            btSB7.setVisibility(View.GONE);
            btSB7.setEnabled(false);
            btSB7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB9.setVisibility(View.GONE);
            btSB9.setEnabled(false);
            btSB9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST6, ST7, ST8 e ST9 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFE1F;
            btST6.setVisibility(View.GONE);
            btST6.setEnabled(false);
            btST6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST7.setVisibility(View.GONE);
            btST7.setEnabled(false);
            btST7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST8.setVisibility(View.GONE);
            btST8.setEnabled(false);
            btST8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST9.setVisibility(View.GONE);
            btST9.setEnabled(false);
            btST9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        if (((mMedidoresAtivos >> 21) & 0x01) == 0x01)
        {
            // Medidor 21 ativo
            // bit 20 ligado indicando medidor Bifásico SB9 ativo
            MedidoresBifasicosDisponiveis &= 0xFEFF; // indicando SB9 indisponível
            btSB9.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSB9.setVisibility(View.VISIBLE);
            btSB9.setEnabled(true);

            // monofásico SM9 e SM10 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xFCFF;
            btSM9.setVisibility(View.GONE);
            btSM9.setEnabled(false);
            btSM9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM10.setVisibility(View.GONE);
            btSM10.setEnabled(false);
            btSM10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB8 e SB10 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xFD7F;
            btSB8.setVisibility(View.GONE);
            btSB8.setEnabled(false);
            btSB8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB10.setVisibility(View.GONE);
            btSB10.setEnabled(false);
            btSB10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST7, ST8, ST9 e ST10 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFC3F;
            btST7.setVisibility(View.GONE);
            btST7.setEnabled(false);
            btST7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST8.setVisibility(View.GONE);
            btST8.setEnabled(false);
            btST8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST9.setVisibility(View.GONE);
            btST9.setEnabled(false);
            btST9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST10.setVisibility(View.GONE);
            btST10.setEnabled(false);
            btST10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        if (((mMedidoresAtivos >> 22) & 0x01) == 0x01)
        {
            // Medidor 22 ativo
            // bit 21 ligado indicando medidor Bifásico SB10 ativo
            MedidoresBifasicosDisponiveis &= 0xFDFF; // indicando SB10 indisponível
            btSB10.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSB10.setVisibility(View.VISIBLE);
            btSB10.setEnabled(true);

            // monofásico SM10 e SM11 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xF9FF;
            btSM10.setVisibility(View.GONE);
            btSM10.setEnabled(false);
            btSM10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM11.setVisibility(View.GONE);
            btSM11.setEnabled(false);
            btSM11.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB9 e SB11 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xFAFF;
            btSB9.setVisibility(View.GONE);
            btSB9.setEnabled(false);
            btSB9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB11.setVisibility(View.GONE);
            btSB11.setEnabled(false);
            btSB11.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST8, ST9 e ST10 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFC7F;
            btST8.setVisibility(View.GONE);
            btST8.setEnabled(false);
            btST8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST9.setVisibility(View.GONE);
            btST9.setEnabled(false);
            btST9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST10.setVisibility(View.GONE);
            btST10.setEnabled(false);
            btST10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        if (((mMedidoresAtivos >> 23) & 0x01) == 0x01)
        {
            // Medidor 23 ativo
            // bit 22 ligado indicando medidor Bifásico SB11 ativo
            MedidoresBifasicosDisponiveis &= 0xFBFF; // indicando SB11 indisponível
            btSB11.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSB11.setVisibility(View.VISIBLE);
            btSB11.setEnabled(true);

            // monofásico SM11 e SM12 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xF3FF;
            btSM11.setVisibility(View.GONE);
            btSM11.setEnabled(false);
            btSM11.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM12.setVisibility(View.GONE);
            btSM12.setEnabled(false);
            btSM12.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB10 não pode ser utilizado
            MedidoresBifasicosDisponiveis &= 0xFDFF;
            btSB10.setVisibility(View.GONE);
            btSB10.setEnabled(false);
            btSB10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST9 e ST10 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFCFF;
            btST9.setVisibility(View.GONE);
            btST9.setEnabled(false);
            btST9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST10.setVisibility(View.GONE);
            btST10.setEnabled(false);
            btST10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }
        //------------------------------------------------------------
        // Fim da configuração das saídas bifásicas
        //------------------------------------------------------------

        //------------------------------------------------------------
        // Inicio da configuração das saídas Trifásicas
        //------------------------------------------------------------
        if (((mMedidoresAtivos >> 24) & 0x01) == 0x01)
        {
            // Medidor 24 ativo
            // bit 23 ligado indicando medidor monofásico ST1 ativo
            MedidoresTrifasicosDisponiveis &= 0xFFFE; // indicando ST1 indisponível
            btST1.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btST1.setVisibility(View.VISIBLE);
            btST1.setEnabled(true);

            // monofásico SM1, SM2 e SM3 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xFFF8;
            btSM1.setVisibility(View.GONE);
            btSM1.setEnabled(false);
            btSM1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM2.setVisibility(View.GONE);
            btSM2.setEnabled(false);
            btSM2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM3.setVisibility(View.GONE);
            btSM3.setEnabled(false);
            btSM3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB1, SB2 e SB3 não pode ser utilizado
            MedidoresBifasicosDisponiveis &= 0xFFF8;
            btSB1.setVisibility(View.GONE);
            btSB1.setEnabled(false);
            btSB1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB2.setVisibility(View.GONE);
            btSB2.setEnabled(false);
            btSB2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB3.setVisibility(View.GONE);
            btSB3.setEnabled(false);
            btSB3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST2 e ST3 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFFF9;
            btST2.setVisibility(View.GONE);
            btST2.setEnabled(false);
            btST2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST3.setVisibility(View.GONE);
            btST3.setEnabled(false);
            btST3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // Hexafásico SH1 não pode ser utilizado
            MedidoresHexafasicosDisponiveis &= 0xFFFE;
            btSH1.setVisibility(View.GONE);
            btSH1.setEnabled(false);
            btSH1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        if (((mMedidoresAtivos >> 25) & 0x01) == 0x01)
        {
            // Medidor 25 ativo
            // bit 24 ligado indicando medidor Trifásico ST2 ativo
            MedidoresTrifasicosDisponiveis &= 0xFFFD; // indicando ST2 indisponível
            btST2.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btST2.setVisibility(View.VISIBLE);
            btST2.setEnabled(true);

            // monofásico SM2, SM3 e SM4 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xFFF1;
            btSM2.setVisibility(View.GONE);
            btSM2.setEnabled(false);
            btSM2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM3.setVisibility(View.GONE);
            btSM3.setEnabled(false);
            btSM3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM4.setVisibility(View.GONE);
            btSM4.setEnabled(false);
            btSM4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB1, SB2, SB3 e SB4 não pode ser utilizado
            MedidoresBifasicosDisponiveis &= 0xFFF0;
            btSB1.setVisibility(View.GONE);
            btSB1.setEnabled(false);
            btSB1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB2.setVisibility(View.GONE);
            btSB2.setEnabled(false);
            btSB2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB3.setVisibility(View.GONE);
            btSB3.setEnabled(false);
            btSB3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB4.setVisibility(View.GONE);
            btSB4.setEnabled(false);
            btSB4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST1, ST3 e ST4 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFFF2;
            btST1.setVisibility(View.GONE);
            btST1.setEnabled(false);
            btST1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST3.setVisibility(View.GONE);
            btST3.setEnabled(false);
            btST3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST4.setVisibility(View.GONE);
            btST4.setEnabled(false);
            btST4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // Hexafásico SH1 não pode ser utilizado
            MedidoresHexafasicosDisponiveis &= 0xFFFE;
            btSH1.setVisibility(View.GONE);
            btSH1.setEnabled(false);
            btSH1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        if (((mMedidoresAtivos >> 26) & 0x01) == 0x01)
        {
            // Medidor 26 ativo
            // bit 25 ligado indicando medidor Trifásico ST3 ativo
            MedidoresTrifasicosDisponiveis &= 0xFFFB; // indicando ST3 indisponível
            btST3.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btST3.setVisibility(View.VISIBLE);
            btST3.setEnabled(true);

            // monofásico SM3, SM4 e SM5 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xFFE3;
            btSM3.setVisibility(View.GONE);
            btSM3.setEnabled(false);
            btSM3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM4.setVisibility(View.GONE);
            btSM4.setEnabled(false);
            btSM4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM5.setVisibility(View.GONE);
            btSM5.setEnabled(false);
            btSM5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB2, SB3, SB4 e SB5 não pode ser utilizado
            MedidoresBifasicosDisponiveis &= 0xFFE1;
            btSB2.setVisibility(View.GONE);
            btSB2.setEnabled(false);
            btSB2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB3.setVisibility(View.GONE);
            btSB3.setEnabled(false);
            btSB3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB4.setVisibility(View.GONE);
            btSB4.setEnabled(false);
            btSB4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB5.setVisibility(View.GONE);
            btSB5.setEnabled(false);
            btSB5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST1, ST2, ST4 e ST5 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFFE4;
            btST1.setVisibility(View.GONE);
            btST1.setEnabled(false);
            btST1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST2.setVisibility(View.GONE);
            btST2.setEnabled(false);
            btST2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST4.setVisibility(View.GONE);
            btST4.setEnabled(false);
            btST4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST5.setVisibility(View.GONE);
            btST5.setEnabled(false);
            btST5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // Hexafásico SH1 não pode ser utilizado
            MedidoresHexafasicosDisponiveis &= 0xFFFE;
            btSH1.setVisibility(View.GONE);
            btSH1.setEnabled(false);
            btSH1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        if (((mMedidoresAtivos >> 27) & 0x01) == 0x01)
        {
            // Medidor 27 ativo
            // bit 26 ligado indicando medidor Trifásico ST4 ativo
            MedidoresTrifasicosDisponiveis &= 0xFFF7; // indicando ST4 indisponível
            btST4.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btST4.setVisibility(View.VISIBLE);
            btST4.setEnabled(true);

            // monofásico SM4, SM5 e SM6 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xFFC7;
            btSM4.setVisibility(View.GONE);
            btSM4.setEnabled(false);
            btSM4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM5.setVisibility(View.GONE);
            btSM5.setEnabled(false);
            btSM5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM6.setVisibility(View.GONE);
            btSM6.setEnabled(false);
            btSM6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB3, SB4, SB5 e SB6 não pode ser utilizado
            MedidoresBifasicosDisponiveis &= 0xFFC3;
            btSB3.setVisibility(View.GONE);
            btSB3.setEnabled(false);
            btSB3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB4.setVisibility(View.GONE);
            btSB4.setEnabled(false);
            btSB4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB5.setVisibility(View.GONE);
            btSB5.setEnabled(false);
            btSB5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB6.setVisibility(View.GONE);
            btSB6.setEnabled(false);
            btSB6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST2, ST3, ST5 e ST6 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFFC9;
            btST2.setVisibility(View.GONE);
            btST2.setEnabled(false);
            btST2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST3.setVisibility(View.GONE);
            btST3.setEnabled(false);
            btST3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST5.setVisibility(View.GONE);
            btST5.setEnabled(false);
            btST5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST6.setVisibility(View.GONE);
            btST6.setEnabled(false);
            btST6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // Hexafásico SH1 não pode ser utilizado
            MedidoresHexafasicosDisponiveis &= 0xFFFE;
            btSH1.setVisibility(View.GONE);
            btSH1.setEnabled(false);
            btSH1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        if (((mMedidoresAtivos >> 28) & 0x01) == 0x01)
        {
            // Medidor 28 ativo
            // bit 27 ligado indicando medidor Trifásico ST5 ativo
            MedidoresTrifasicosDisponiveis &= 0xFFEF; // indicando ST5 indisponível
            btST5.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btST5.setVisibility(View.VISIBLE);
            btST5.setEnabled(true);

            // monofásico SM5, SM6 e SM7 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xFF8F;
            btSM5.setVisibility(View.GONE);
            btSM5.setEnabled(false);
            btSM5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM6.setVisibility(View.GONE);
            btSM6.setEnabled(false);
            btSM6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM7.setVisibility(View.GONE);
            btSM7.setEnabled(false);
            btSM7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB4, SB5, SB6 e SB7 não pode ser utilizado
            MedidoresBifasicosDisponiveis &= 0xFF87;
            btSB4.setVisibility(View.GONE);
            btSB4.setEnabled(false);
            btSB4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB5.setVisibility(View.GONE);
            btSB5.setEnabled(false);
            btSB5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB6.setVisibility(View.GONE);
            btSB6.setEnabled(false);
            btSB6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB7.setVisibility(View.GONE);
            btSB7.setEnabled(false);
            btSB7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST3, ST4, ST6 e ST7 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFF93;
            btST3.setVisibility(View.GONE);
            btST3.setEnabled(false);
            btST3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST4.setVisibility(View.GONE);
            btST4.setEnabled(false);
            btST4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST6.setVisibility(View.GONE);
            btST6.setEnabled(false);
            btST6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST7.setVisibility(View.GONE);
            btST7.setEnabled(false);
            btST7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // Hexafásico SH1 não pode ser utilizado
            MedidoresHexafasicosDisponiveis &= 0xFFFE;
            btSH1.setVisibility(View.GONE);
            btSH1.setEnabled(false);
            btSH1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        if (((mMedidoresAtivos >> 29) & 0x01) == 0x01)
        {
            // Medidor 29 ativo
            // bit 28 ligado indicando medidor Trifásico ST6 ativo
            MedidoresTrifasicosDisponiveis &= 0xFFDF; // indicando ST6 indisponível
            btST6.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btST6.setVisibility(View.VISIBLE);
            btST6.setEnabled(true);

            // monofásico SM6, SM7 e SM8 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xFF1F;
            btSM6.setVisibility(View.GONE);
            btSM6.setEnabled(false);
            btSM6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM7.setVisibility(View.GONE);
            btSM7.setEnabled(false);
            btSM7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM8.setVisibility(View.GONE);
            btSM8.setEnabled(false);
            btSM8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB5, SB6, SB7 e SB8 não pode ser utilizado
            MedidoresBifasicosDisponiveis &= 0xFF0F;
            btSB5.setVisibility(View.GONE);
            btSB5.setEnabled(false);
            btSB5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB6.setVisibility(View.GONE);
            btSB6.setEnabled(false);
            btSB6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB7.setVisibility(View.GONE);
            btSB7.setEnabled(false);
            btSB7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB8.setVisibility(View.GONE);
            btSB8.setEnabled(false);
            btSB8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST4, ST5, ST7 e ST8 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFF27;
            btST4.setVisibility(View.GONE);
            btST4.setEnabled(false);
            btST4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST5.setVisibility(View.GONE);
            btST5.setEnabled(false);
            btST5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST7.setVisibility(View.GONE);
            btST7.setEnabled(false);
            btST7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST8.setVisibility(View.GONE);
            btST8.setEnabled(false);
            btST8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // Hexafásico SH1 não pode ser utilizado
            MedidoresHexafasicosDisponiveis &= 0xFFFE;
            btSH1.setVisibility(View.GONE);
            btSH1.setEnabled(false);
            btSH1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        if (((mMedidoresAtivos >> 30) & 0x01) == 0x01)
        {
            // Medidor 30 ativo
            // bit 29 ligado indicando medidor Trifásico ST7 ativo
            MedidoresTrifasicosDisponiveis &= 0xFFBF; // indicando ST7 indisponível
            btST7.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btST7.setVisibility(View.VISIBLE);
            btST7.setEnabled(true);

            // monofásico SM7, SM8 e SM9 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xFE3F;
            btSM7.setVisibility(View.GONE);
            btSM7.setEnabled(false);
            btSM7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM8.setVisibility(View.GONE);
            btSM8.setEnabled(false);
            btSM8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM9.setVisibility(View.GONE);
            btSM9.setEnabled(false);
            btSM9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB6, SB7, SB8 e SB9 não pode ser utilizado
            MedidoresBifasicosDisponiveis &= 0xFE1F;
            btSB6.setVisibility(View.GONE);
            btSB6.setEnabled(false);
            btSB6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB7.setVisibility(View.GONE);
            btSB7.setEnabled(false);
            btSB7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB8.setVisibility(View.GONE);
            btSB8.setEnabled(false);
            btSB8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB9.setVisibility(View.GONE);
            btSB9.setEnabled(false);
            btSB9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST5, ST6, ST8 e ST9 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFE4F;
            btST5.setVisibility(View.GONE);
            btST5.setEnabled(false);
            btST5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST6.setVisibility(View.GONE);
            btST6.setEnabled(false);
            btST6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST8.setVisibility(View.GONE);
            btST8.setEnabled(false);
            btST8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST9.setVisibility(View.GONE);
            btST9.setEnabled(false);
            btST9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        if (((mMedidoresAtivos >> 31) & 0x01) == 0x01)
        {
            // Medidor 31 ativo
            // bit 30 ligado indicando medidor Trifásico ST8 ativo
            MedidoresTrifasicosDisponiveis &= 0xFF7F; // indicando ST8 indisponível
            btST8.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btST8.setVisibility(View.VISIBLE);
            btST8.setEnabled(true);

            // monofásico SM8, SM9 e SM10 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xFC3F;
            btSM8.setVisibility(View.GONE);
            btSM8.setEnabled(false);
            btSM8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM9.setVisibility(View.GONE);
            btSM9.setEnabled(false);
            btSM9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM10.setVisibility(View.GONE);
            btSM10.setEnabled(false);
            btSM10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB7, SB8, SB9 e SB10 não pode ser utilizado
            MedidoresBifasicosDisponiveis &= 0xFC3F;
            btSB7.setVisibility(View.GONE);
            btSB7.setEnabled(false);
            btSB7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB8.setVisibility(View.GONE);
            btSB8.setEnabled(false);
            btSB8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB9.setVisibility(View.GONE);
            btSB9.setEnabled(false);
            btSB9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB10.setVisibility(View.GONE);
            btSB10.setEnabled(false);
            btSB10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST6, ST7, ST9 e ST10 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFC9F;
            btST6.setVisibility(View.GONE);
            btST6.setEnabled(false);
            btST6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST7.setVisibility(View.GONE);
            btST7.setEnabled(false);
            btST7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST9.setVisibility(View.GONE);
            btST9.setEnabled(false);
            btST9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST10.setVisibility(View.GONE);
            btST10.setEnabled(false);
            btST10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        if (((mMedidoresAtivos >> 32) & 0x01) == 0x01)
        {
            // Medidor 32 ativo
            // bit 31 ligado indicando medidor Trifásico ST9 ativo
            MedidoresTrifasicosDisponiveis &= 0xFEFF; // indicando ST9 indisponível
            btST9.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btST9.setVisibility(View.VISIBLE);
            btST9.setEnabled(true);

            // monofásico SM9, SM10 e SM11 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xF8FF;
            btSM9.setVisibility(View.GONE);
            btSM9.setEnabled(false);
            btSM9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM10.setVisibility(View.GONE);
            btSM10.setEnabled(false);
            btSM10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM11.setVisibility(View.GONE);
            btSM11.setEnabled(false);
            btSM11.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB8, SB9, SB10 e SB11 não pode ser utilizado
            MedidoresBifasicosDisponiveis &= 0xF87F;
            btSB8.setVisibility(View.GONE);
            btSB8.setEnabled(false);
            btSB8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB9.setVisibility(View.GONE);
            btSB9.setEnabled(false);
            btSB9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB10.setVisibility(View.GONE);
            btSB10.setEnabled(false);
            btSB10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB11.setVisibility(View.GONE);
            btSB11.setEnabled(false);
            btSB11.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST7, ST8 e ST10 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFD3F;
            btST7.setVisibility(View.GONE);
            btST7.setEnabled(false);
            btST7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST8.setVisibility(View.GONE);
            btST8.setEnabled(false);
            btST8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST10.setVisibility(View.GONE);
            btST10.setEnabled(false);
            btST10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        if (((mMedidoresAtivos >> 33) & 0x01) == 0x01)
        {
            // Medidor 33 ativo
            // bit 32 ligado indicando medidor Trifásico ST10 ativo
            MedidoresTrifasicosDisponiveis &= 0xFDFF; // indicando ST10 indisponível
            btST10.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btST10.setVisibility(View.VISIBLE);
            btST10.setEnabled(true);

            // monofásico SM10, SM11 e SM12 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xF1FF;
            btSM10.setVisibility(View.GONE);
            btSM10.setEnabled(false);
            btSM10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM11.setVisibility(View.GONE);
            btSM11.setEnabled(false);
            btSM11.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM12.setVisibility(View.GONE);
            btSM12.setEnabled(false);
            btSM12.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB9, SB10 e SB11 não pode ser utilizado
            MedidoresBifasicosDisponiveis &= 0xF8FF;
            btSB9.setVisibility(View.GONE);
            btSB9.setEnabled(false);
            btSB9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB10.setVisibility(View.GONE);
            btSB10.setEnabled(false);
            btSB10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB11.setVisibility(View.GONE);
            btSB11.setEnabled(false);
            btSB11.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST8 e ST9 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFE7F;
            btST8.setVisibility(View.GONE);
            btST8.setEnabled(false);
            btST8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST9.setVisibility(View.GONE);
            btST9.setEnabled(false);
            btST9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }
        //------------------------------------------------------------
        // Fim da configuração das saídas Trifásicas
        //------------------------------------------------------------

        QtdMonofasicosDisponiveis = 0;
        for (int j = 0; j < 12; j++)
        {
            if (((MedidoresMonofasicosDisponiveis >> j) & 0x01) == 0x01)
            {
                QtdMonofasicosDisponiveis++;
            }
        }

        QtdBifasicosDisponiveis = 0;
        for (int j = 0; j < 11; j++)
        {
            if (((MedidoresBifasicosDisponiveis >> j) & 0x01) == 0x01)
            {
                QtdBifasicosDisponiveis++;
            }
        }

        QtdTrifasicosDisponiveis = 0;
        for (int j = 0; j < 10; j++)
        {
            if (((MedidoresTrifasicosDisponiveis >> j) & 0x01) == 0x01)
            {
                QtdTrifasicosDisponiveis++;
            }
        }

        QtdHexafasicosDisponiveis = 0;
        for (int j = 0; j < 1; j++)
        {
            if (((MedidoresHexafasicosDisponiveis >> j) & 0x01) == 0x01)
            {
                QtdHexafasicosDisponiveis++;
            }
        }
    }
}

/*
package com.starmeasure.absoluto;


import static android.view.View.VISIBLE;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import com.starmeasure.protocoloabsoluto.ComandoAbsoluto;
import com.starmeasure.protocoloabsoluto.MedidorAbsoluto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ConfiguracaoMedidor extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private static Button btSM1;
    private static Button btSM2;
    private static Button btSM3;
    private static Button btSM4;
    private static Button btSM5;
    private static Button btSM6;
    private static Button btSM7;
    private static Button btSM8;
    private static Button btSM9;
    private static Button btSM10;
    private static Button btSM11;
    private static Button btSM12;
    private static Button btSB1;
    private static Button btSB2;
    private static Button btSB3;
    private static Button btSB4;
    private static Button btSB5;
    private static Button btSB6;
    private static Button btSB7;
    private static Button btSB8;
    private static Button btSB9;
    private static Button btSB10;
    private static Button btSB11;
    private static Button btST1;
    private static Button btST2;
    private static Button btST3;
    private static Button btST4;
    private static Button btST5;
    private static Button btST6;
    private static Button btST7;
    private static Button btST8;
    private static Button btST9;
    private static Button btST10;

    private static Button btSH1;

    private static EditText etUC;
    private static EditText etIU;
    private static EditText etNumeroMedidor;
    private static EditText etDI;

    private static ArrayList<MedidorAbsoluto> medidores;

    int mMedidores1;
    int mMedidores2;
    int mMedidores3;
    int mMedidores4;
    int mMedidores5;
    int mMedidores6;
    int mMedidores7;
    int mMedidores8;

    int QtdMonofasicosDisponiveis = 0;
    int QtdBifasicosDisponiveis = 0;
    int QtdTrifasicosDisponiveis = 0;
    int QtdHexafasicosDisponiveis = 0;

    int MedidoresMonofasicosDisponiveis = 0xFFFF;     // 12 saídas indisponíveis (SM0... SM12)
    int MedidoresBifasicosDisponiveis = 0x07FF;  // 11 saídas disponíveis (SB1... SB11)
    int MedidoresTrifasicosDisponiveis = 0x03FF; // 10 saídas disponíveis (ST1... ST10)
    int MedidoresHexafasicosDisponiveis = 0x0001;// 1 saída disponível (SH1)

    Spinner mTipoLigacao;

    private long tempoEnvio = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuracao);

        btSM1 = findViewById(R.id.btnS1);
        btSM2 = findViewById(R.id.btnS2);
        btSM3 = findViewById(R.id.btnS3);
        btSM4 = findViewById(R.id.btnS4);
        btSM5 = findViewById(R.id.btnS5);
        btSM6 = findViewById(R.id.btnS6);
        btSM7 = findViewById(R.id.btnS7);
        btSM8 = findViewById(R.id.btnS8);
        btSM9 = findViewById(R.id.btnS9);
        btSM10 = findViewById(R.id.btnS10);
        btSM11 = findViewById(R.id.btnS11);
        btSM12 = findViewById(R.id.btnS12);

        btSB1 = findViewById(R.id.btnS0102);
        btSB2 = findViewById(R.id.btnS0203);
        btSB3 = findViewById(R.id.btnS0304);
        btSB4 = findViewById(R.id.btnS0405);
        btSB5 = findViewById(R.id.btnS0506);
        btSB6 = findViewById(R.id.btnS0607);
        btSB7 = findViewById(R.id.btnS0708);
        btSB8 = findViewById(R.id.btnS0809);
        btSB9 = findViewById(R.id.btnS0910);
        btSB10 = findViewById(R.id.btnS1011);
        btSB11 = findViewById(R.id.btnS1112);

        btST1 = findViewById(R.id.btnS010203);
        btST2 = findViewById(R.id.btnS020304);
        btST3 = findViewById(R.id.btnS030405);
        btST4 = findViewById(R.id.btnS040506);
        btST5 = findViewById(R.id.btnS050607);
        btST6 = findViewById(R.id.btnS060708);
        btST7 = findViewById(R.id.btnS070809);
        btST8 = findViewById(R.id.btnS080910);
        btST9 = findViewById(R.id.btnS091011);
        btST10 = findViewById(R.id.btnS101112);

        btSH1 = findViewById(R.id.btnSH1);

        etUC = findViewById(R.id.etNumeroUC);
        etIU = findViewById(R.id.etNumeroIU);
        etNumeroMedidor = findViewById(R.id.etNumeroMedidor);

        btSM1.setOnClickListener(this);
        btSM2.setOnClickListener(this);
        btSM3.setOnClickListener(this);
        btSM4.setOnClickListener(this);
        btSM5.setOnClickListener(this);
        btSM6.setOnClickListener(this);
        btSM7.setOnClickListener(this);
        btSM8.setOnClickListener(this);
        btSM9.setOnClickListener(this);
        btSM10.setOnClickListener(this);
        btSM11.setOnClickListener(this);
        btSM12.setOnClickListener(this);

        btSB1.setOnClickListener(this);
        btSB2.setOnClickListener(this);
        btSB3.setOnClickListener(this);
        btSB4.setOnClickListener(this);
        btSB5.setOnClickListener(this);
        btSB6.setOnClickListener(this);
        btSB7.setOnClickListener(this);
        btSB8.setOnClickListener(this);
        btSB9.setOnClickListener(this);
        btSB10.setOnClickListener(this);
        btSB11.setOnClickListener(this);

        btST1.setOnClickListener(this);
        btST2.setOnClickListener(this);
        btST3.setOnClickListener(this);
        btST4.setOnClickListener(this);
        btST5.setOnClickListener(this);
        btST6.setOnClickListener(this);
        btST7.setOnClickListener(this);
        btST8.setOnClickListener(this);
        btST9.setOnClickListener(this);
        btST10.setOnClickListener(this);

        btSH1.setOnClickListener(this);

        final Intent intent = getIntent();
        mMedidores1 = intent.getIntExtra(Consts.EXTRAS_MEDIDORES1, mMedidores1);
        mMedidores2 = intent.getIntExtra(Consts.EXTRAS_MEDIDORES2, mMedidores2);
        mMedidores3 = intent.getIntExtra(Consts.EXTRAS_MEDIDORES3, mMedidores3);
        mMedidores4 = intent.getIntExtra(Consts.EXTRAS_MEDIDORES4, mMedidores4);
        mMedidores5 = intent.getIntExtra(Consts.EXTRAS_MEDIDORES5, mMedidores5);
        mMedidores6 = intent.getIntExtra(Consts.EXTRAS_MEDIDORES6, mMedidores6);
        mMedidores7 = intent.getIntExtra(Consts.EXTRAS_MEDIDORES7, mMedidores7);
        mMedidores8 = intent.getIntExtra(Consts.EXTRAS_MEDIDORES8, mMedidores8);
        medidores = (ArrayList<MedidorAbsoluto>) getIntent().getExtras().getSerializable(Consts.EXTRAS_MEDIDORES);

        long mMedidoresAtivos = 0;
        mMedidoresAtivos = mMedidores8 << 56;
        mMedidoresAtivos |= mMedidores7 << 48;
        mMedidoresAtivos |= mMedidores6 << 40;
        mMedidoresAtivos |= mMedidores5 << 32;
        mMedidoresAtivos |= mMedidores4 << 24;
        mMedidoresAtivos |= mMedidores3 << 16;
        mMedidoresAtivos |= mMedidores2 << 8;
        mMedidoresAtivos |= mMedidores1;

        //------------------------------------------------------------
        // Inicio da configuração das saídas monofásicas
        //------------------------------------------------------------
        if(((mMedidoresAtivos >> 1) & 0x01) == 0x01)
        {
            // Medidor 1 ativo
            // bit 0 ligado indicando medidor monofásico SM1 ativo
            MedidoresMonofasicosDisponiveis &= 0xFFFE;
            btSM1.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSM1.setVisibility(View.VISIBLE);
            btSM1.setEnabled(true);

            // bifásico SB1 não pode ser utilizado
            MedidoresBifasicosDisponiveis &= 0xFFFE;
            btSB1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
            btSB1.setEnabled(false);
            btSB1.setVisibility(View.GONE);

            // trifásico ST1 não pode ser utilizado
            MedidoresTrifasicosDisponiveis &= 0xFFFE;
            btST1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
            btST1.setEnabled(false);
            btST1.setVisibility(View.GONE);

            // Hexafásico SH1 não pode ser utilizado
            MedidoresHexafasicosDisponiveis &= 0xFFFE;
            btSH1.setVisibility(View.GONE);
            btSH1.setEnabled(false);
            btSH1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }
        if(((mMedidoresAtivos >> 2) & 0x01) == 0x01)
        {
            // Medidor 2 ativo
            // bit 1 ligado indicando medidor monofásico SM2 ativo
            MedidoresMonofasicosDisponiveis &= 0xFFFD;
            btSM2.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSM2.setEnabled(true);
            btSM2.setVisibility(VISIBLE);

            // bifásico SB1 e SB2 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xFFFC;
            btSB1.setVisibility(View.GONE);
            btSB1.setEnabled(false);
            btSB1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            //clGlobal.Medidores2[14].bAtivo = false;
            btSB2.setVisibility(View.GONE);
            btSB2.setEnabled(false);
            btSB2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST1 e ST2 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFFFC;
            btST1.setVisibility(View.GONE);
            btST1.setEnabled(false);
            btST1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            //clGlobal.Medidores2[25].bAtivo = false;
            btST2.setVisibility(View.GONE);
            btST2.setEnabled(false);
            btST2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // Hexafásico SH1 não pode ser utilizado
            MedidoresHexafasicosDisponiveis &= 0xFFFE;
            btSH1.setVisibility(View.GONE);
            btSH1.setEnabled(false);
            btSH1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }
        if(((mMedidoresAtivos >> 3) & 0x01) == 0x01)
        {
            // Medidor 3 ativo
            // bit 2 ligado indicando medidor monofásico SM3 ativo
            MedidoresMonofasicosDisponiveis &= 0xFFFB;
            btSM3.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSM3.setVisibility(View.VISIBLE);
            btSM3.setEnabled(true);

            // bifásico SB2 e SB3 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xFFF9;
            btSB2.setVisibility(View.GONE);
            btSB2.setEnabled(false);
            btSB2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            //clGlobal.Medidores2[15].bAtivo = false;
            btSB3.setVisibility(View.GONE);
            btSB3.setEnabled(false);
            btSB3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST1, ST2 e ST3 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFFF8;
            btST1.setVisibility(View.GONE);
            btST1.setEnabled(false);
            btST1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST2.setVisibility(View.GONE);
            btST2.setEnabled(false);
            btST2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST3.setVisibility(View.GONE);
            btST3.setEnabled(false);
            btST3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // Hexafásico SH1 não pode ser utilizado
            MedidoresHexafasicosDisponiveis &= 0xFFFE;
            btSH1.setVisibility(View.GONE);
            btSH1.setEnabled(false);
            btSH1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }
        if(((mMedidoresAtivos >> 4) & 0x01) == 0x01)
        {
            // Medidor 4 ativo
            // bit 3 ligado indicando medidor monofásico SM4 ativo
            MedidoresMonofasicosDisponiveis &= 0xFFF7;
            btSM4.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSM4.setVisibility(View.VISIBLE);
            btSM4.setEnabled(true);

            // bifásico SB3 e SB4 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xFFF3;
            btSB3.setVisibility(View.GONE);
            btSB3.setEnabled(false);
            btSB3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB4.setVisibility(View.GONE);
            btSB4.setEnabled(false);
            btSB4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST2, ST3 e ST4 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFFF1;
            btST2.setVisibility(View.GONE);
            btST2.setEnabled(false);
            btST2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST3.setVisibility(View.GONE);
            btST3.setEnabled(false);
            btST3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST4.setVisibility(View.GONE);
            btST4.setEnabled(false);
            btST4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // Hexafásico SH1 não pode ser utilizado
            MedidoresHexafasicosDisponiveis &= 0xFFFE;
            btSH1.setVisibility(View.GONE);
            btSH1.setEnabled(false);
            btSH1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }
        if(((mMedidoresAtivos >> 5) & 0x01) == 0x01)
        {
            // Medidor 5 ativo
            // bit 4 ligado indicando medidor monofásico SM5 ativo
            MedidoresMonofasicosDisponiveis &= 0xFFEF;
            btSM5.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSM5.setVisibility(View.VISIBLE);
            btSM5.setEnabled(true);

            // bifásico SB4 e SB5 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xFFE7;
            btSB4.setVisibility(View.GONE);
            btSB4.setEnabled(false);
            btSB4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB5.setVisibility(View.GONE);
            btSB5.setEnabled(false);
            btSB5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST3, ST4 e ST5 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFFE3;
            btST3.setVisibility(View.GONE);
            btST3.setEnabled(false);
            btST3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST4.setVisibility(View.GONE);
            btST4.setEnabled(false);
            btST4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST5.setVisibility(View.GONE);
            btST5.setEnabled(false);
            btST5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // Hexafásico SH1 não pode ser utilizado
            MedidoresHexafasicosDisponiveis &= 0xFFFE;
            btSH1.setVisibility(View.GONE);
            btSH1.setEnabled(false);
            btSH1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }
        if(((mMedidoresAtivos >> 6) & 0x01) == 0x01)
        {
            // Medidor 6 ativo
            // bit 5 ligado indicando medidor monofásico SM6 ativo
            MedidoresMonofasicosDisponiveis &= 0xFFDF;
            btSM6.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSM6.setVisibility(View.VISIBLE);
            btSM6.setEnabled(true);

            // bifásico SB5 e SB6 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xFFCF;
            btSB5.setVisibility(View.GONE);
            btSB5.setEnabled(false);
            btSB5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB6.setVisibility(View.GONE);
            btSB6.setEnabled(false);
            btSB6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST4, ST5 e ST6 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFFC7;
            btST4.setVisibility(View.GONE);
            btST4.setEnabled(false);
            btST4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST5.setVisibility(View.GONE);
            btST5.setEnabled(false);
            btST5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST6.setVisibility(View.GONE);
            btST6.setEnabled(false);
            btST6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // Hexafásico SH1 não pode ser utilizado
            MedidoresHexafasicosDisponiveis &= 0xFFFE;
            btSH1.setVisibility(View.GONE);
            btSH1.setEnabled(false);
            btSH1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }
        if(((mMedidoresAtivos >> 7) & 0x01) == 0x01)
        {
            // Medidor 7 ativo
            // bit 6 ligado indicando medidor monofásico SM7 ativo
            MedidoresMonofasicosDisponiveis &= 0xFFBF;
            btSM7.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSM7.setVisibility(View.VISIBLE);
            btSM7.setEnabled(true);

            // bifásico SB6 e SB7 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xFF9F;
            btSB6.setVisibility(View.GONE);
            btSB6.setEnabled(false);
            btSB6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB7.setVisibility(View.GONE);
            btSB7.setEnabled(false);
            btSB7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST5, ST6 e ST7 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFF8F;
            btST5.setVisibility(View.GONE);
            btST5.setEnabled(false);
            btST5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST6.setVisibility(View.GONE);
            btST6.setEnabled(false);
            btST6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST7.setVisibility(View.GONE);
            btST7.setEnabled(false);
            btST7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }
        if(((mMedidoresAtivos >> 8) & 0x01) == 0x01)
        {
            // Medidor 8 ativo
            // bit 7 ligado indicando medidor monofásico SM8 ativo
            MedidoresMonofasicosDisponiveis &= 0xFF7F;
            btSM8.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSM8.setVisibility(View.VISIBLE);
            btSM8.setEnabled(true);

            // bifásico SB7 e SB8 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xFF3F;
            btSB7.setVisibility(View.GONE);
            btSB7.setEnabled(false);
            btSB7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB8.setVisibility(View.GONE);
            btSB8.setEnabled(false);
            btSB8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST6, ST7 e ST8 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFF1F;
            btST6.setVisibility(View.GONE);
            btST6.setEnabled(false);
            btST6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST7.setVisibility(View.GONE);
            btST7.setEnabled(false);
            btST7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST8.setVisibility(View.GONE);
            btST8.setEnabled(false);
            btST8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }
        if(((mMedidoresAtivos >> 9) & 0x01) == 0x01)
        {
            // Medidor 9 ativo
            // bit 0 ligado indicando medidor monofásico SM9 ativo
            MedidoresMonofasicosDisponiveis &= 0xFEFF;
            btSM9.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSM9.setVisibility(View.VISIBLE);
            btSM9.setEnabled(true);

            // bifásico SB8 e SB9 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xFE7F;
            btSB8.setVisibility(View.GONE);
            btSB8.setEnabled(false);
            btSB8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB9.setVisibility(View.GONE);
            btSB9.setEnabled(false);
            btSB9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST7, ST8 e ST9 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFE3F;
            btST7.setVisibility(View.GONE);
            btST7.setEnabled(false);
            btST7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST8.setVisibility(View.GONE);
            btST8.setEnabled(false);
            btST8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST9.setVisibility(View.GONE);
            btST9.setEnabled(false);
            btST9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }
        if(((mMedidoresAtivos >> 10) & 0x01) == 0x01)
        {
            // Medidor 10 ativo
            // bit 1 ligado indicando medidor monofásico SM10 ativo
            MedidoresMonofasicosDisponiveis &= 0xFDFF;
            btSM10.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSM10.setVisibility(View.VISIBLE);
            btSM10.setEnabled(true);

            // bifásico SB9 e SB10 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xFCFF;
            btSB9.setVisibility(View.GONE);
            btSB9.setEnabled(false);
            btSB9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB10.setVisibility(View.GONE);
            btSB10.setEnabled(false);
            btSB10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST8, ST9 e ST10 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFC7F;
            btST8.setVisibility(View.GONE);
            btST8.setEnabled(false);
            btST8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST9.setVisibility(View.GONE);
            btST9.setEnabled(false);
            btST9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST10.setVisibility(View.GONE);
            btST10.setEnabled(false);
            btST10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }
        if(((mMedidoresAtivos >> 11) & 0x01) == 0x01)
        {
            // Medidor 11 ativo
            // bit 2 ligado indicando medidor monofásico SM11 ativo
            MedidoresMonofasicosDisponiveis &= 0xFBFF;
            btSM11.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSM11.setVisibility(View.VISIBLE);
            btSM11.setEnabled(true);

            // bifásico SB10 e SB11 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xF9FF;
            btSB10.setVisibility(View.GONE);
            btSB10.setEnabled(false);
            btSB10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB11.setVisibility(View.GONE);
            btSB11.setEnabled(false);
            btSB11.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST9 e ST10 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFCFF;
            btST9.setVisibility(View.GONE);
            btST9.setEnabled(false);
            btST9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST10.setVisibility(View.GONE);
            btST10.setEnabled(false);
            btST10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }
        if(((mMedidoresAtivos >> 12) & 0x01) == 0x01)
        {
            // Medidor 12 ativo
            // bit 3 ligado indicando medidor monofásico SM12 ativo
            MedidoresMonofasicosDisponiveis &= 0xF7FF;
            btSM12.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSM12.setVisibility(View.VISIBLE);
            btSM12.setEnabled(true);

            // bifásico SB11 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xFBFF;
            btSB11.setVisibility(View.GONE);
            btSB11.setEnabled(false);
            btSB11.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST10 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFDFF;
            btST10.setVisibility(View.GONE);
            btST10.setEnabled(false);
            btST10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }
        //------------------------------------------------------------
        // Fim da configuração das saídas monofásicas
        //------------------------------------------------------------

        //------------------------------------------------------------
        // Inicio da configuração das saídas bifásicas
        //------------------------------------------------------------
        if (((mMedidoresAtivos >> 13) & 0x01) == 0x01)
        {
            // Medidor 13 ativo
            // bit 12 ligado indicando medidor monofásico SB1 ativo
            MedidoresBifasicosDisponiveis &= 0xFFFE; // indicando SB1 indisponível
            btSB1.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSB1.setVisibility(View.VISIBLE);
            btSB1.setEnabled(true);

            // monofásico SM1 e SM2 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xFFFC;
            btSM1.setVisibility(View.GONE);
            btSM1.setEnabled(false);
            btSM1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM2.setVisibility(View.GONE);
            btSM2.setEnabled(false);
            btSM2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB2 não pode ser utilizado
            MedidoresBifasicosDisponiveis &= 0xFFFD;
            btSB2.setVisibility(View.GONE);
            btSB2.setEnabled(false);
            btSB2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST1 e ST2 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFFFC;
            btST1.setVisibility(View.GONE);
            btST1.setEnabled(false);
            btST1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST2.setVisibility(View.GONE);
            btST2.setEnabled(false);
            btST2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // Hexafásico SH1 não pode ser utilizado
            MedidoresHexafasicosDisponiveis &= 0xFFFE;
            btSH1.setVisibility(View.GONE);
            btSH1.setEnabled(false);
            btSH1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        if (((mMedidoresAtivos >> 14) & 0x01) == 0x01)
        {
            // Medidor 14 ativo
            // bit 13 ligado indicando medidor monofásico SB2 ativo
            MedidoresBifasicosDisponiveis &= 0xFFFD; // indicando SB2 indisponível
            btSB2.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSB2.setVisibility(View.VISIBLE);
            btSB2.setEnabled(true);

            // monofásico SM2 e SM3 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xFFF9;
            btSM2.setVisibility(View.GONE);
            btSM2.setEnabled(false);
            btSM2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM3.setVisibility(View.GONE);
            btSM3.setEnabled(false);
            btSM3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB1 e SB3 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xFFFA;
            btSB1.setVisibility(View.GONE);
            btSB1.setEnabled(false);
            btSB1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB3.setVisibility(View.GONE);
            btSB3.setEnabled(false);
            btSB3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST1, ST2 e ST3 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFFF8;
            btST1.setVisibility(View.GONE);
            btST1.setEnabled(false);
            btST1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST2.setVisibility(View.GONE);
            btST2.setEnabled(false);
            btST2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST3.setVisibility(View.GONE);
            btST3.setEnabled(false);
            btST3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // Hexafásico SH1 não pode ser utilizado
            MedidoresHexafasicosDisponiveis &= 0xFFFE;
            btSH1.setVisibility(View.GONE);
            btSH1.setEnabled(false);
            btSH1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        if (((mMedidoresAtivos >> 15) & 0x01) == 0x01)
        {
            // Medidor 15 ativo
            // bit 14 ligado indicando medidor monofásico SB3 ativo
            MedidoresBifasicosDisponiveis &= 0xFFFB; // indicando SB3 indisponível
            btSB3.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSB3.setVisibility(View.VISIBLE);
            btSB3.setEnabled(true);

            // monofásico SM3 e SM4 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xFFF3;
            btSM3.setVisibility(View.GONE);
            btSM3.setEnabled(false);
            btSM3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM4.setVisibility(View.GONE);
            btSM4.setEnabled(false);
            btSM4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB2 e SB4 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xFFF5;
            btSB2.setVisibility(View.GONE);
            btSB2.setEnabled(false);
            btSB2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB4.setVisibility(View.GONE);
            btSB4.setEnabled(false);
            btSB4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST1, ST2, ST3 e ST4 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFFF0;
            btST1.setVisibility(View.GONE);
            btST1.setEnabled(false);
            btST1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST2.setVisibility(View.GONE);
            btST2.setEnabled(false);
            btST2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST3.setVisibility(View.GONE);
            btST3.setEnabled(false);
            btST3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST4.setVisibility(View.GONE);
            btST4.setEnabled(false);
            btST4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // Hexafásico SH1 não pode ser utilizado
            MedidoresHexafasicosDisponiveis &= 0xFFFE;
            btSH1.setVisibility(View.GONE);
            btSH1.setEnabled(false);
            btSH1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        if (((mMedidoresAtivos >> 16) & 0x01) == 0x01)
        {
            // Medidor 16 ativo
            // bit 15 ligado indicando medidor monofásico SB4 ativo
            MedidoresBifasicosDisponiveis &= 0xFFF7; // indicando SB4 indisponível
            btSB4.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSB4.setVisibility(View.VISIBLE);
            btSB4.setEnabled(true);

            // monofásico SM4 e SM5 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xFFE7;
            btSM4.setVisibility(View.GONE);
            btSM4.setEnabled(false);
            btSM4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM5.setVisibility(View.GONE);
            btSM5.setEnabled(false);
            btSM5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB3 e SB5 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xFFEB;
            btSB3.setVisibility(View.GONE);
            btSB3.setEnabled(false);
            btSB3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB5.setVisibility(View.GONE);
            btSB5.setEnabled(false);
            btSB5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST2, ST3, ST4 e ST5 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFFE1;
            btST2.setVisibility(View.GONE);
            btST2.setEnabled(false);
            btST2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST3.setVisibility(View.GONE);
            btST3.setEnabled(false);
            btST3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST4.setVisibility(View.GONE);
            btST4.setEnabled(false);
            btST4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST5.setVisibility(View.GONE);
            btST5.setEnabled(false);
            btST5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // Hexafásico SH1 não pode ser utilizado
            MedidoresHexafasicosDisponiveis &= 0xFFFE;
            btSH1.setVisibility(View.GONE);
            btSH1.setEnabled(false);
            btSH1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        if (((mMedidoresAtivos >> 17) & 0x01) == 0x01)
        {
            // Medidor 17 ativo
            // bit 16 ligado indicando medidor monofásico SB5 ativo
            MedidoresBifasicosDisponiveis &= 0xFFEF; // indicando SB5 indisponível
            btSB5.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSB5.setVisibility(View.VISIBLE);
            btSB5.setEnabled(true);

            // monofásico SM5 e SM6 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xFFCF;
            btSM5.setVisibility(View.GONE);
            btSM5.setEnabled(false);
            btSM5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM6.setVisibility(View.GONE);
            btSM6.setEnabled(false);
            btSM6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB4 e SB6 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xFFD7;
            btSB4.setVisibility(View.GONE);
            btSB4.setEnabled(false);
            btSB4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB6.setVisibility(View.GONE);
            btSB6.setEnabled(false);
            btSB6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST3, ST4, ST5 e ST6 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFFC3;
            btST3.setVisibility(View.GONE);
            btST3.setEnabled(false);
            btST3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST4.setVisibility(View.GONE);
            btST4.setEnabled(false);
            btST4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST5.setVisibility(View.GONE);
            btST5.setEnabled(false);
            btST5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST6.setVisibility(View.GONE);
            btST6.setEnabled(false);
            btST6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // Hexafásico SH1 não pode ser utilizado
            MedidoresHexafasicosDisponiveis &= 0xFFFE;
            btSH1.setVisibility(View.GONE);
            btSH1.setEnabled(false);
            btSH1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        if (((mMedidoresAtivos >> 18) & 0x01) == 0x01)
        {
            // Medidor 18 ativo
            // bit 17 ligado indicando medidor monofásico SB6 ativo
            MedidoresBifasicosDisponiveis &= 0xFFDF; // indicando SB6 indisponível
            btSB6.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSB6.setVisibility(View.VISIBLE);
            btSB6.setEnabled(true);

            // monofásico SM6 e SM7 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xFF9F;
            btSM6.setVisibility(View.GONE);
            btSM6.setEnabled(false);
            btSM6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM7.setVisibility(View.GONE);
            btSM7.setEnabled(false);
            btSM7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB5 e SB7 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xFFAF;
            btSB5.setVisibility(View.GONE);
            btSB5.setEnabled(false);
            btSB5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB7.setVisibility(View.GONE);
            btSB7.setEnabled(false);
            btSB7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST4, ST5, ST6 e ST7 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFF87;
            btST4.setVisibility(View.GONE);
            btST4.setEnabled(false);
            btST4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST5.setVisibility(View.GONE);
            btST5.setEnabled(false);
            btST5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST6.setVisibility(View.GONE);
            btST6.setEnabled(false);
            btST6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST7.setVisibility(View.GONE);
            btST7.setEnabled(false);
            btST7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // Hexafásico SH1 não pode ser utilizado
            MedidoresHexafasicosDisponiveis &= 0xFFFE;
            btSH1.setVisibility(View.GONE);
            btSH1.setEnabled(false);
            btSH1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        if (((mMedidoresAtivos >> 19) & 0x01) == 0x01)
        {
            // Medidor 19 ativo
            // bit 18 ligado indicando medidor monofásico SB7 ativo
            MedidoresBifasicosDisponiveis &= 0xFFBF; // indicando SB7 indisponível
            btSB7.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSB7.setVisibility(View.VISIBLE);
            btSB7.setEnabled(true);

            // monofásico SM7 e SM8 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xFF3F;
            btSM7.setVisibility(View.GONE);
            btSM7.setEnabled(false);
            btSM7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM8.setVisibility(View.GONE);
            btSM8.setEnabled(false);
            btSM8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB6 e SB8 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xFF5F;
            btSB6.setVisibility(View.GONE);
            btSB6.setEnabled(false);
            btSB6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB8.setVisibility(View.GONE);
            btSB8.setEnabled(false);
            btSB8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST5, ST6, ST7 e ST8 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFF0F;
            btST5.setVisibility(View.GONE);
            btST5.setEnabled(false);
            btST5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST6.setVisibility(View.GONE);
            btST6.setEnabled(false);
            btST6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST7.setVisibility(View.GONE);
            btST7.setEnabled(false);
            btST7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST8.setVisibility(View.GONE);
            btST8.setEnabled(false);
            btST8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        if (((mMedidoresAtivos >> 20) & 0x01) == 0x01)
        {
            // Medidor 20 ativo
            // bit 19 ligado indicando medidor monofásico SB8 ativo
            MedidoresBifasicosDisponiveis &= 0xFF7F; // indicando SB8 indisponível
            btSB8.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSB8.setVisibility(View.VISIBLE);
            btSB8.setEnabled(true);

            // monofásico SM8 e SM9 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xFE7F;
            btSM8.setVisibility(View.GONE);
            btSM8.setEnabled(false);
            btSM8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM9.setVisibility(View.GONE);
            btSM9.setEnabled(false);
            btSM9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB7 e SB9 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xFEBF;
            btSB7.setVisibility(View.GONE);
            btSB7.setEnabled(false);
            btSB7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB9.setVisibility(View.GONE);
            btSB9.setEnabled(false);
            btSB9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST6, ST7, ST8 e ST9 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFE1F;
            btST6.setVisibility(View.GONE);
            btST6.setEnabled(false);
            btST6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST7.setVisibility(View.GONE);
            btST7.setEnabled(false);
            btST7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST8.setVisibility(View.GONE);
            btST8.setEnabled(false);
            btST8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST9.setVisibility(View.GONE);
            btST9.setEnabled(false);
            btST9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        if (((mMedidoresAtivos >> 21) & 0x01) == 0x01)
        {
            // Medidor 21 ativo
            // bit 20 ligado indicando medidor monofásico SB9 ativo
            MedidoresBifasicosDisponiveis &= 0xFEFF; // indicando SB9 indisponível
            btSB9.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSB9.setVisibility(View.VISIBLE);
            btSB9.setEnabled(true);

            // monofásico SM9 e SM10 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xFCFF;
            btSM9.setVisibility(View.GONE);
            btSM9.setEnabled(false);
            btSM9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM10.setVisibility(View.GONE);
            btSM10.setEnabled(false);
            btSM10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB8 e SB10 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xFD7F;
            btSB8.setVisibility(View.GONE);
            btSB8.setEnabled(false);
            btSB8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB10.setVisibility(View.GONE);
            btSB10.setEnabled(false);
            btSB10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST7, ST8, ST9 e ST10 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFC3F;
            btST7.setVisibility(View.GONE);
            btST7.setEnabled(false);
            btST7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST8.setVisibility(View.GONE);
            btST8.setEnabled(false);
            btST8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST9.setVisibility(View.GONE);
            btST9.setEnabled(false);
            btST9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST10.setVisibility(View.GONE);
            btST10.setEnabled(false);
            btST10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        if (((mMedidoresAtivos >> 22) & 0x01) == 0x01)
        {
            // Medidor 22 ativo
            // bit 21 ligado indicando medidor monofásico SB10 ativo
            MedidoresBifasicosDisponiveis &= 0xFDFF; // indicando SB10 indisponível
            btSB10.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSB10.setVisibility(View.VISIBLE);
            btSB10.setEnabled(true);

            // monofásico SM10 e SM11 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xF9FF;
            btSM10.setVisibility(View.GONE);
            btSM10.setEnabled(false);
            btSM10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM11.setVisibility(View.GONE);
            btSM11.setEnabled(false);
            btSM11.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB9 e SB11 não podem ser utilizados
            MedidoresBifasicosDisponiveis &= 0xFAFF;
            btSB9.setVisibility(View.GONE);
            btSB9.setEnabled(false);
            btSB9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB11.setVisibility(View.GONE);
            btSB11.setEnabled(false);
            btSB11.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST8, ST9 e ST10 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFC7F;
            btST8.setVisibility(View.GONE);
            btST8.setEnabled(false);
            btST8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST9.setVisibility(View.GONE);
            btST9.setEnabled(false);
            btST9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST10.setVisibility(View.GONE);
            btST10.setEnabled(false);
            btST10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        if (((mMedidoresAtivos >> 23) & 0x01) == 0x01)
        {
            // Medidor 23 ativo
            // bit 22 ligado indicando medidor monofásico SB11 ativo
            MedidoresBifasicosDisponiveis &= 0xFBFF; // indicando SB11 indisponível
            btSB11.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btSB11.setVisibility(View.VISIBLE);
            btSB11.setEnabled(true);

            // monofásico SM11 e SM12 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xF3FF;
            btSM11.setVisibility(View.GONE);
            btSM11.setEnabled(false);
            btSM11.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM12.setVisibility(View.GONE);
            btSM12.setEnabled(false);
            btSM12.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB10 não pode ser utilizado
            MedidoresBifasicosDisponiveis &= 0xFDFF;
            btSB10.setVisibility(View.GONE);
            btSB10.setEnabled(false);
            btSB10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST9 e ST10 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFCFF;
            btST9.setVisibility(View.GONE);
            btST9.setEnabled(false);
            btST9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST10.setVisibility(View.GONE);
            btST10.setEnabled(false);
            btST10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }
        //------------------------------------------------------------
        // Fim da configuração das saídas bifásicas
        //------------------------------------------------------------

        //------------------------------------------------------------
        // Inicio da configuração das saídas trifásicas
        //------------------------------------------------------------
        if (((mMedidoresAtivos >> 24) & 0x01) == 0x01)
        {
            // Medidor 24 ativo
            // bit 23 ligado indicando medidor monofásico ST1 ativo
            MedidoresTrifasicosDisponiveis &= 0xFFFE; // indicando ST1 indisponível
            btST1.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btST1.setVisibility(View.VISIBLE);
            btST1.setEnabled(true);

            // monofásico SM1, SM2 e SM3 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xFFF8;
            btSM1.setVisibility(View.GONE);
            btSM1.setEnabled(false);
            btSM1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM2.setVisibility(View.GONE);
            btSM2.setEnabled(false);
            btSM2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM3.setVisibility(View.GONE);
            btSM3.setEnabled(false);
            btSM3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB1, SB2 e SB3 não pode ser utilizado
            MedidoresBifasicosDisponiveis &= 0xFFF8;
            btSB1.setVisibility(View.GONE);
            btSB1.setEnabled(false);
            btSB1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB2.setVisibility(View.GONE);
            btSB2.setEnabled(false);
            btSB2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB3.setVisibility(View.GONE);
            btSB3.setEnabled(false);
            btSB3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST2 e ST3 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFFF9;
            btST2.setVisibility(View.GONE);
            btST2.setEnabled(false);
            btST2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST3.setVisibility(View.GONE);
            btST3.setEnabled(false);
            btST3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // Hexafásico SH1 não pode ser utilizado
            MedidoresHexafasicosDisponiveis &= 0xFFFE;
            btSH1.setVisibility(View.GONE);
            btSH1.setEnabled(false);
            btSH1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        if (((mMedidoresAtivos >> 25) & 0x01) == 0x01)
        {
            // Medidor 25 ativo
            // bit 24 ligado indicando medidor monofásico ST2 ativo
            MedidoresTrifasicosDisponiveis &= 0xFFFD; // indicando ST2 indisponível
            btST2.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btST2.setVisibility(View.VISIBLE);
            btST2.setEnabled(true);

            // monofásico SM2, SM3 e SM4 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xFFF1;
            btSM2.setVisibility(View.GONE);
            btSM2.setEnabled(false);
            btSM2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM3.setVisibility(View.GONE);
            btSM3.setEnabled(false);
            btSM3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM4.setVisibility(View.GONE);
            btSM4.setEnabled(false);
            btSM4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB1, SB2, SB3 e SB4 não pode ser utilizado
            MedidoresBifasicosDisponiveis &= 0xFFF0;
            btSB1.setVisibility(View.GONE);
            btSB1.setEnabled(false);
            btSB1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB2.setVisibility(View.GONE);
            btSB2.setEnabled(false);
            btSB2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB3.setVisibility(View.GONE);
            btSB3.setEnabled(false);
            btSB3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB4.setVisibility(View.GONE);
            btSB4.setEnabled(false);
            btSB4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST1, ST3 e ST4 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFFF2;
            btST1.setVisibility(View.GONE);
            btST1.setEnabled(false);
            btST1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST3.setVisibility(View.GONE);
            btST3.setEnabled(false);
            btST3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST4.setVisibility(View.GONE);
            btST4.setEnabled(false);
            btST4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // Hexafásico SH1 não pode ser utilizado
            MedidoresHexafasicosDisponiveis &= 0xFFFE;
            btSH1.setVisibility(View.GONE);
            btSH1.setEnabled(false);
            btSH1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        if (((mMedidoresAtivos >> 26) & 0x01) == 0x01)
        {
            // Medidor 26 ativo
            // bit 25 ligado indicando medidor monofásico ST3 ativo
            MedidoresTrifasicosDisponiveis &= 0xFFFB; // indicando ST3 indisponível
            btST3.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btST3.setVisibility(View.VISIBLE);
            btST3.setEnabled(true);

            // monofásico SM3, SM4 e SM5 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xFFE3;
            btSM3.setVisibility(View.GONE);
            btSM3.setEnabled(false);
            btSM3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM4.setVisibility(View.GONE);
            btSM4.setEnabled(false);
            btSM4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM5.setVisibility(View.GONE);
            btSM5.setEnabled(false);
            btSM5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB2, SB3, SB4 e SB5 não pode ser utilizado
            MedidoresBifasicosDisponiveis &= 0xFFE1;
            btSB2.setVisibility(View.GONE);
            btSB2.setEnabled(false);
            btSB2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB3.setVisibility(View.GONE);
            btSB3.setEnabled(false);
            btSB3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB4.setVisibility(View.GONE);
            btSB4.setEnabled(false);
            btSB4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB5.setVisibility(View.GONE);
            btSB5.setEnabled(false);
            btSB5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST1, ST2, ST4 e ST5 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFFE4;
            btST1.setVisibility(View.GONE);
            btST1.setEnabled(false);
            btST1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST2.setVisibility(View.GONE);
            btST2.setEnabled(false);
            btST2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST4.setVisibility(View.GONE);
            btST4.setEnabled(false);
            btST4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST5.setVisibility(View.GONE);
            btST5.setEnabled(false);
            btST5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // Hexafásico SH1 não pode ser utilizado
            MedidoresHexafasicosDisponiveis &= 0xFFFE;
            btSH1.setVisibility(View.GONE);
            btSH1.setEnabled(false);
            btSH1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        if (((mMedidoresAtivos >> 27) & 0x01) == 0x01)
        {
            // Medidor 27 ativo
            // bit 26 ligado indicando medidor monofásico ST4 ativo
            MedidoresTrifasicosDisponiveis &= 0xFFF7; // indicando ST4 indisponível
            btST4.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btST4.setVisibility(View.VISIBLE);
            btST4.setEnabled(true);

            // monofásico SM4, SM5 e SM6 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xFFC7;
            btSM4.setVisibility(View.GONE);
            btSM4.setEnabled(false);
            btSM4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM5.setVisibility(View.GONE);
            btSM5.setEnabled(false);
            btSM5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM6.setVisibility(View.GONE);
            btSM6.setEnabled(false);
            btSM6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB3, SB4, SB5 e SB6 não pode ser utilizado
            MedidoresBifasicosDisponiveis &= 0xFFC3;
            btSB3.setVisibility(View.GONE);
            btSB3.setEnabled(false);
            btSB3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB4.setVisibility(View.GONE);
            btSB4.setEnabled(false);
            btSB4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB5.setVisibility(View.GONE);
            btSB5.setEnabled(false);
            btSB5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB6.setVisibility(View.GONE);
            btSB6.setEnabled(false);
            btSB6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST2, ST3, ST5 e ST6 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFFC9;
            btST2.setVisibility(View.GONE);
            btST2.setEnabled(false);
            btST2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST3.setVisibility(View.GONE);
            btST3.setEnabled(false);
            btST3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST5.setVisibility(View.GONE);
            btST5.setEnabled(false);
            btST5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST6.setVisibility(View.GONE);
            btST6.setEnabled(false);
            btST6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // Hexafásico SH1 não pode ser utilizado
            MedidoresHexafasicosDisponiveis &= 0xFFFE;
            btSH1.setVisibility(View.GONE);
            btSH1.setEnabled(false);
            btSH1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        if (((mMedidoresAtivos >> 28) & 0x01) == 0x01)
        {
            // Medidor 28 ativo
            // bit 27 ligado indicando medidor monofásico ST5 ativo
            MedidoresTrifasicosDisponiveis &= 0xFFEF; // indicando ST5 indisponível
            btST5.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btST5.setVisibility(View.VISIBLE);
            btST5.setEnabled(true);

            // monofásico SM5, SM6 e SM7 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xFF8F;
            btSM5.setVisibility(View.GONE);
            btSM5.setEnabled(false);
            btSM5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM6.setVisibility(View.GONE);
            btSM6.setEnabled(false);
            btSM6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM7.setVisibility(View.GONE);
            btSM7.setEnabled(false);
            btSM7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB4, SB5, SB6 e SB7 não pode ser utilizado
            MedidoresBifasicosDisponiveis &= 0xFF87;
            btSB4.setVisibility(View.GONE);
            btSB4.setEnabled(false);
            btSB4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB5.setVisibility(View.GONE);
            btSB5.setEnabled(false);
            btSB5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB6.setVisibility(View.GONE);
            btSB6.setEnabled(false);
            btSB6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB7.setVisibility(View.GONE);
            btSB7.setEnabled(false);
            btSB7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST3, ST4, ST6 e ST7 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFF93;
            btST3.setVisibility(View.GONE);
            btST3.setEnabled(false);
            btST3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST4.setVisibility(View.GONE);
            btST4.setEnabled(false);
            btST4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST6.setVisibility(View.GONE);
            btST6.setEnabled(false);
            btST6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST7.setVisibility(View.GONE);
            btST7.setEnabled(false);
            btST7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // Hexafásico SH1 não pode ser utilizado
            MedidoresHexafasicosDisponiveis &= 0xFFFE;
            btSH1.setVisibility(View.GONE);
            btSH1.setEnabled(false);
            btSH1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        if (((mMedidoresAtivos >> 29) & 0x01) == 0x01)
        {
            // Medidor 29 ativo
            // bit 28 ligado indicando medidor monofásico ST6 ativo
            MedidoresTrifasicosDisponiveis &= 0xFFDF; // indicando ST6 indisponível
            btST6.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btST6.setVisibility(View.VISIBLE);
            btST6.setEnabled(true);

            // monofásico SM6, SM7 e SM8 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xFF1F;
            btSM6.setVisibility(View.GONE);
            btSM6.setEnabled(false);
            btSM6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM7.setVisibility(View.GONE);
            btSM7.setEnabled(false);
            btSM7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM8.setVisibility(View.GONE);
            btSM8.setEnabled(false);
            btSM8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB5, SB6, SB7 e SB8 não pode ser utilizado
            MedidoresBifasicosDisponiveis &= 0xFF0F;
            btSB5.setVisibility(View.GONE);
            btSB5.setEnabled(false);
            btSB5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB6.setVisibility(View.GONE);
            btSB6.setEnabled(false);
            btSB6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB7.setVisibility(View.GONE);
            btSB7.setEnabled(false);
            btSB7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB8.setVisibility(View.GONE);
            btSB8.setEnabled(false);
            btSB8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST4, ST5, ST7 e ST8 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFF27;
            btST4.setVisibility(View.GONE);
            btST4.setEnabled(false);
            btST4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST5.setVisibility(View.GONE);
            btST5.setEnabled(false);
            btST5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST7.setVisibility(View.GONE);
            btST7.setEnabled(false);
            btST7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST8.setVisibility(View.GONE);
            btST8.setEnabled(false);
            btST8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // Hexafásico SH1 não pode ser utilizado
            MedidoresHexafasicosDisponiveis &= 0xFFFE;
            btSH1.setVisibility(View.GONE);
            btSH1.setEnabled(false);
            btSH1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        if (((mMedidoresAtivos >> 30) & 0x01) == 0x01)
        {
            // Medidor 30 ativo
            // bit 29 ligado indicando medidor monofásico ST7 ativo
            MedidoresTrifasicosDisponiveis &= 0xFFBF; // indicando ST7 indisponível
            btST7.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btST7.setVisibility(View.VISIBLE);
            btST7.setEnabled(true);

            // monofásico SM7, SM8 e SM9 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xFE3F;
            btSM7.setVisibility(View.GONE);
            btSM7.setEnabled(false);
            btSM7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM8.setVisibility(View.GONE);
            btSM8.setEnabled(false);
            btSM8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM9.setVisibility(View.GONE);
            btSM9.setEnabled(false);
            btSM9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB6, SB7, SB8 e SB9 não pode ser utilizado
            MedidoresBifasicosDisponiveis &= 0xFE1F;
            btSB6.setVisibility(View.GONE);
            btSB6.setEnabled(false);
            btSB6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB7.setVisibility(View.GONE);
            btSB7.setEnabled(false);
            btSB7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB8.setVisibility(View.GONE);
            btSB8.setEnabled(false);
            btSB8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB9.setVisibility(View.GONE);
            btSB9.setEnabled(false);
            btSB9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST5, ST6, ST8 e ST9 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFE4F;
            btST5.setVisibility(View.GONE);
            btST5.setEnabled(false);
            btST5.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST6.setVisibility(View.GONE);
            btST6.setEnabled(false);
            btST6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST8.setVisibility(View.GONE);
            btST8.setEnabled(false);
            btST8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST9.setVisibility(View.GONE);
            btST9.setEnabled(false);
            btST9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        if (((mMedidoresAtivos >> 31) & 0x01) == 0x01)
        {
            // Medidor 31 ativo
            // bit 30 ligado indicando medidor monofásico ST8 ativo
            MedidoresTrifasicosDisponiveis &= 0xFF7F; // indicando ST8 indisponível
            btST8.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btST8.setVisibility(View.VISIBLE);
            btST8.setEnabled(true);

            // monofásico SM8, SM9 e SM10 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xFC3F;
            btSM8.setVisibility(View.GONE);
            btSM8.setEnabled(false);
            btSM8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM9.setVisibility(View.GONE);
            btSM9.setEnabled(false);
            btSM9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM10.setVisibility(View.GONE);
            btSM10.setEnabled(false);
            btSM10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB7, SB8, SB9 e SB10 não pode ser utilizado
            MedidoresBifasicosDisponiveis &= 0xFC3F;
            btSB7.setVisibility(View.GONE);
            btSB7.setEnabled(false);
            btSB7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB8.setVisibility(View.GONE);
            btSB8.setEnabled(false);
            btSB8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB9.setVisibility(View.GONE);
            btSB9.setEnabled(false);
            btSB9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB10.setVisibility(View.GONE);
            btSB10.setEnabled(false);
            btSB10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST6, ST7, ST9 e ST10 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFC9F;
            btST6.setVisibility(View.GONE);
            btST6.setEnabled(false);
            btST6.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST7.setVisibility(View.GONE);
            btST7.setEnabled(false);
            btST7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST9.setVisibility(View.GONE);
            btST9.setEnabled(false);
            btST9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST10.setVisibility(View.GONE);
            btST10.setEnabled(false);
            btST10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        if (((mMedidoresAtivos >> 32) & 0x01) == 0x01)
        {
            // Medidor 32 ativo
            // bit 31 ligado indicando medidor monofásico ST9 ativo
            MedidoresTrifasicosDisponiveis &= 0xFEFF; // indicando ST9 indisponível
            btST9.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btST9.setVisibility(View.VISIBLE);
            btST9.setEnabled(true);

            // monofásico SM9, SM10 e SM11 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xF8FF;
            btSM9.setVisibility(View.GONE);
            btSM9.setEnabled(false);
            btSM9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM10.setVisibility(View.GONE);
            btSM10.setEnabled(false);
            btSM10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM11.setVisibility(View.GONE);
            btSM11.setEnabled(false);
            btSM11.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB8, SB9, SB10 e SB11 não pode ser utilizado
            MedidoresBifasicosDisponiveis &= 0xF87F;
            btSB8.setVisibility(View.GONE);
            btSB8.setEnabled(false);
            btSB8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB9.setVisibility(View.GONE);
            btSB9.setEnabled(false);
            btSB9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB10.setVisibility(View.GONE);
            btSB10.setEnabled(false);
            btSB10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB11.setVisibility(View.GONE);
            btSB11.setEnabled(false);
            btSB11.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST7, ST8 e ST10 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFD3F;
            btST7.setVisibility(View.GONE);
            btST7.setEnabled(false);
            btST7.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST8.setVisibility(View.GONE);
            btST8.setEnabled(false);
            btST8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST10.setVisibility(View.GONE);
            btST10.setEnabled(false);
            btST10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        if (((mMedidoresAtivos >> 33) & 0x01) == 0x01)
        {
            // Medidor 33 ativo
            // bit 32 ligado indicando medidor monofásico ST10 ativo
            MedidoresTrifasicosDisponiveis &= 0xFDFF; // indicando ST10 indisponível
            btST10.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            btST10.setVisibility(View.VISIBLE);
            btST10.setEnabled(true);

            // monofásico SM10, SM11 e SM12 não podem ser utilizados
            MedidoresMonofasicosDisponiveis &= 0xF1FF;
            btSM10.setVisibility(View.GONE);
            btSM10.setEnabled(false);
            btSM10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM11.setVisibility(View.GONE);
            btSM11.setEnabled(false);
            btSM11.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSM12.setVisibility(View.GONE);
            btSM12.setEnabled(false);
            btSM12.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // bifásico SB9, SB10 e SB11 não pode ser utilizado
            MedidoresBifasicosDisponiveis &= 0xF8FF;
            btSB9.setVisibility(View.GONE);
            btSB9.setEnabled(false);
            btSB9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB10.setVisibility(View.GONE);
            btSB10.setEnabled(false);
            btSB10.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btSB11.setVisibility(View.GONE);
            btSB11.setEnabled(false);
            btSB11.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            // trifásico ST8 e ST9 não podem ser utilizados
            MedidoresTrifasicosDisponiveis &= 0xFE7F;
            btST8.setVisibility(View.GONE);
            btST8.setEnabled(false);
            btST8.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            btST9.setVisibility(View.GONE);
            btST9.setEnabled(false);
            btST9.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }
        //------------------------------------------------------------
        // Fim da configuração das saídas Trifásicas
        //------------------------------------------------------------

        QtdMonofasicosDisponiveis = 0;
        for (int j = 0; j < 12; j++)
        {
            if (((MedidoresMonofasicosDisponiveis >> j) & 0x01) == 0x01)
            {
                QtdMonofasicosDisponiveis++;
            }
        }

        QtdBifasicosDisponiveis = 0;
        for (int j = 0; j < 11; j++)
        {
            if (((MedidoresBifasicosDisponiveis >> j) & 0x01) == 0x01)
            {
                QtdBifasicosDisponiveis++;
            }
        }

        QtdTrifasicosDisponiveis = 0;
        for (int j = 0; j < 10; j++)
        {
            if (((MedidoresTrifasicosDisponiveis >> j) & 0x01) == 0x01)
            {
                QtdTrifasicosDisponiveis++;
            }
        }

        QtdHexafasicosDisponiveis = 0;
        for (int j = 0; j < 1; j++)
        {
            if (((MedidoresHexafasicosDisponiveis >> j) & 0x01) == 0x01)
            {
                QtdHexafasicosDisponiveis++;
            }
        }

        mTipoLigacao = findViewById(R.id.spinner_tipo_ligacao);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.tipo_ligacao_medidor));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTipoLigacao.setAdapter(adapter);
        mTipoLigacao.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String sTipoLigacao = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        String UCAux = "";
        ColorStateList CorLidaBotao;
        if(v != null){
            if(v.getId() == R.id.btnS1){
                CorLidaBotao = v.getBackgroundTintList();
                if(CorLidaBotao == ColorStateList.valueOf(Color.RED)){
                    UCAux = medidores.get(1).unidadeConsumidora;
                    etUC.setText(UCAux);
                    etNumeroMedidor.setText(medidores.get(1).numero);
                    //etIU.setText();
                }
                Toast.makeText(this, "Botão SM1", Toast.LENGTH_SHORT).show();
            }
            else if(v.getId() == R.id.btnS2){
                CorLidaBotao = v.getBackgroundTintList();
                if(CorLidaBotao == ColorStateList.valueOf(Color.RED)){
                    UCAux = medidores.get(2).unidadeConsumidora;
                    etUC.setText(UCAux);
                    etNumeroMedidor.setText(medidores.get(2).numero);
                    //etIU.setText();
                }
                Toast.makeText(this, "Botão SM2", Toast.LENGTH_SHORT).show();
            }
            else if(v.getId() == R.id.btnS3){
                CorLidaBotao = v.getBackgroundTintList();
                if(CorLidaBotao == ColorStateList.valueOf(Color.RED)){
                    UCAux = medidores.get(3).unidadeConsumidora;
                    etUC.setText(UCAux);
                    etNumeroMedidor.setText(medidores.get(3).numero);
                    //etIU.setText();
                }
                Toast.makeText(this, "Botão SM3", Toast.LENGTH_SHORT).show();
            }
            else if(v.getId() == R.id.btnS4){
                Toast.makeText(this, "Botão SM4", Toast.LENGTH_SHORT).show();
            }
            else if(v.getId() == R.id.btnS5){
                CorLidaBotao = v.getBackgroundTintList();
                if(CorLidaBotao == ColorStateList.valueOf(Color.RED)){
                    String tipoMedidor = "";
                    for(int z=0;z < medidores.size(); z++){
                        int sTipo = medidores.get(z).tipo;
                        if(sTipo == 5){
                            UCAux = medidores.get(z).unidadeConsumidora;
                            etUC.setText(UCAux);
                            etNumeroMedidor.setText(medidores.get(z).numero);
                            //etIU.setText();
                        }
                    }
                }
                Toast.makeText(this, "Botão SM5", Toast.LENGTH_SHORT).show();
            }
            else if(v.getId() == R.id.btnS6){
                Toast.makeText(this, "Botão SM6", Toast.LENGTH_SHORT).show();
            }
            else if(v.getId() == R.id.btnS7){
                Toast.makeText(this, "Botão SM7", Toast.LENGTH_SHORT).show();
            }
            else if(v.getId() == R.id.btnS8){
                Toast.makeText(this, "Botão SM8", Toast.LENGTH_SHORT).show();
            }
            else if(v.getId() == R.id.btnS9){
                Toast.makeText(this, "Botão SM9", Toast.LENGTH_SHORT).show();
            }
            else if(v.getId() == R.id.btnS10){
                Toast.makeText(this, "Botão SM10", Toast.LENGTH_SHORT).show();
            }
            else if(v.getId() == R.id.btnS11){
                Toast.makeText(this, "Botão SM11", Toast.LENGTH_SHORT).show();
            }
            else if(v.getId() == R.id.btnS12){
                Toast.makeText(this, "Botão SM12", Toast.LENGTH_SHORT).show();
            }
            else if(v.getId() == R.id.btnS0102){
                Toast.makeText(this, "Botão SB1", Toast.LENGTH_SHORT).show();
            }
            else if(v.getId() == R.id.btnS0203){
                Toast.makeText(this, "Botão SB2", Toast.LENGTH_SHORT).show();
            }
            else if(v.getId() == R.id.btnS0304){
                Toast.makeText(this, "Botão SB3", Toast.LENGTH_SHORT).show();
            }
            else if(v.getId() == R.id.btnS0405){
                Toast.makeText(this, "Botão SB4", Toast.LENGTH_SHORT).show();
            }
            else if(v.getId() == R.id.btnS0506){
                Toast.makeText(this, "Botão SB5", Toast.LENGTH_SHORT).show();
            }
            else if(v.getId() == R.id.btnS0607){
                Toast.makeText(this, "Botão SB6", Toast.LENGTH_SHORT).show();
            }
            else if(v.getId() == R.id.btnS0708){
                Toast.makeText(this, "Botão SB7", Toast.LENGTH_SHORT).show();
            }
            else if(v.getId() == R.id.btnS0809){
                Toast.makeText(this, "Botão SB8", Toast.LENGTH_SHORT).show();
            }
            else if(v.getId() == R.id.btnS0910){
                Toast.makeText(this, "Botão SB9", Toast.LENGTH_SHORT).show();
            }
            else if(v.getId() == R.id.btnS1011){
                Toast.makeText(this, "Botão SB10", Toast.LENGTH_SHORT).show();
            }
            else if(v.getId() == R.id.btnS1112){
                Toast.makeText(this, "Botão SB11", Toast.LENGTH_SHORT).show();
            }
            else if(v.getId() == R.id.btnS010203){
                Toast.makeText(this, "Botão ST1", Toast.LENGTH_SHORT).show();
            }
            else if(v.getId() == R.id.btnS020304){
                Toast.makeText(this, "Botão ST2", Toast.LENGTH_SHORT).show();
            }
            else if(v.getId() == R.id.btnS030405){
                Toast.makeText(this, "Botão ST3", Toast.LENGTH_SHORT).show();
            }
            else if(v.getId() == R.id.btnS040506){
                Toast.makeText(this, "Botão ST4", Toast.LENGTH_SHORT).show();
            }
            else if(v.getId() == R.id.btnS050607){
                Toast.makeText(this, "Botão ST5", Toast.LENGTH_SHORT).show();
            }
            else if(v.getId() == R.id.btnS060708){
                Toast.makeText(this, "Botão ST6", Toast.LENGTH_SHORT).show();
            }
            else if(v.getId() == R.id.btnS070809){
                Toast.makeText(this, "Botão ST7", Toast.LENGTH_SHORT).show();
            }
            else if(v.getId() == R.id.btnS080910){
                Toast.makeText(this, "Botão ST8", Toast.LENGTH_SHORT).show();
            }
            else if(v.getId() == R.id.btnS091011){
                Toast.makeText(this, "Botão ST9", Toast.LENGTH_SHORT).show();
            }
            else if(v.getId() == R.id.btnS101112){
                Toast.makeText(this, "Botão ST10", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void enviaComandoAB03(String num_medidor, byte Comando, byte ativacao, int UC, int IU, int IUTC, int metodo) {

        byte[] comando = new ComandoAbsoluto.AB03()
                .comNumerMedidor(num_medidor, metodo)
                .build(metodo, Comando, ativacao, UC, IU, IUTC);
        //tempoEnvio = sendData(comando);
    }
}
*/