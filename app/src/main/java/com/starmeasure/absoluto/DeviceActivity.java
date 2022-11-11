package com.starmeasure.absoluto;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.starmeasure.absoluto.filemanager.FileManagerLeiturasActivity;
import com.starmeasure.absoluto.filemanager.FileManagerNICActivity;
import com.starmeasure.absoluto.filemanager.controller.CargaController;
import com.starmeasure.absoluto.filemanager.model.Carga;
import com.starmeasure.protocoloabsoluto.ComandoAbsoluto;
import com.starmeasure.protocoloabsoluto.MedidorAbsoluto;
import com.starmeasure.protocoloabsoluto.RespostaAbsoluto;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import static android.view.View.*;
import static com.starmeasure.protocoloabsoluto.RespostaAbsoluto.ByteArrayToFloat;


public class DeviceActivity extends AppCompatActivity
        implements PopupMenu.OnMenuItemClickListener {

    private static final String TAG = DeviceActivity.class.getSimpleName();
    public static int NIC = 0;

    private boolean isNewModule = false;


    private Dialog dialogMonitoramento;
    private Dialog dialogConfiguracaoNIC;
    private Dialog dialogCalibracao;
    private Dialog dialogError;
    private ComandoAbsoluto.AB03 mAB03;
    private ComandoAbsoluto.AB05 mAB05;
    private ComandoAbsoluto.AB14 mAB14;
    private ComandoAbsoluto.EB90 mEB90;
    private ComandoAbsoluto.EB17 mEB17;
    private String monitoramentoNumeroEasy;
    private String mDeviceName;
    private String mDeviceAddress;
    private BluetoothLeService mBluetoothLeService;
    private BluetoothGattService mStarMeasureService;
    private MeterListAdapter mMeterListAdapter;
    private MeterEasyTrafoAdapter mMeterEasyTrafoAdapter;
    private RecyclerView mRecyclerView;
    private ImageButton mRefreshStatus;
    private List<MedidorAbsoluto> medidores;
    public class MedidorAbsolutoCom {
        public String[] numMedidor = new String[12];
        public byte metodoNum;
        public float tensaoNominal;
        public byte calPO;

        @Override
        public String toString() {
            return "MedidorAbsoluto{" +
                    "Medidor 0='" + numMedidor[0] + '\'' +
                    ", Medidor 1='" + numMedidor[1] + '\'' +
                    ", Medidor 2='" + numMedidor[2] + '\'' +
                    ", Medidor 3='" + numMedidor[3] + '\'' +
                    ", Medidor 4='" + numMedidor[4] + '\'' +
                    ", Medidor 5='" + numMedidor[5] + '\'' +
                    ", Medidor 6='" + numMedidor[6] + '\'' +
                    ", Medidor 7='" + numMedidor[7] + '\'' +
                    ", Medidor 8='" + numMedidor[8] + '\'' +
                    ", Medidor 9='" + numMedidor[9] + '\'' +
                    ", Medidor 10='" + numMedidor[10] + '\'' +
                    ", Medidor 11='" + numMedidor[11] + '\'' +
                    ", Metodo=" + metodoNum +
                    ", Tensao=" + tensaoNominal +
                    ", CalPO=" + calPO +
                    '}';
        }
    }
    public MedidorAbsolutoCom medidoresCom = new MedidorAbsolutoCom();
    private boolean rodandoCarga = false;
    private boolean rodandoEB90 = false;
    private boolean rodandoEB17 = false;
    private boolean brespostaAB03 = false;
    private boolean brespostaAB05 = false;
    private boolean brespostaEB17 = false;
    private boolean bAlterandoConfigRemota = false;
    private boolean bAlterandoConfigEspecial = false;
    private boolean bUltimoEstadoModoPrateleira = false;
    private boolean isChecked = false;
    private Carga mCarga;
    private ComandoAbsoluto.CargaDePrograma mCargaDePrograma;
    private int contaTentativasContador = 0;
    private int IntervaloDiasMMSM = 0;
    private ImageView mFolderConfig;

    private Handler mHandler;
    private Runnable mLeituraMedidorRunnable;
    private Runnable mMMRunnable;
    private static final long TIMEOUT = 10000;
    private static final long TIMEOUT_MM = 5000;

    private AlertDialog progressDialog;

    boolean bAlteracaoTelefoneDF = false;
    boolean bAlteracaoTelefoneKeepAlive = false;
    String sTelefone1 = "";
    String sTelefone2 = "";
    String sTelefone3 = "";
    String sTelefone4 = "";
    String sTelefoneKeepAlive = "";
    private static String NroMedidorReleComandado = "";
    byte iEventos = 0;
    byte iCiclos = 0;
    byte iRepeticoes = 0;
    int iIntervalo1 = 0;
    int iIntervalo2 = 0;
    byte iValidade = 0;
    byte iModoPrateleira = 0;
    byte iFrequenciaKeepAlive = 0;
    byte iContadorLeituraEB17Enviado = 0;


    byte iItem = 0;

    byte iModoEspecial = 0;
    byte iCalibracao = 0;
    byte iContadorLeituraAb05Enviando = 0;
    byte iContadorLeituraAb03Enviando = 0;
    long medidores_ativos_1 = 0x0000000000000000;
    long medidores_ativos_2 = 0x0000000000000000;
    long medidores_ativos_3 = 0x0000000000000000;
    long medidores_ativos_4 = 0x0000000000000000;
    long medidores_ativos_5 = 0x0000000000000000;
    long medidores_ativos_6 = 0x0000000000000000;
    long medidores_ativos_7 = 0x0000000000000000;
    long medidores_ativos_8 = 0x0000000000000000;
    long medidores_ativos = 0x0000000000000000;
    boolean itest = false;

    public enum TipoOperacao {
        RegistradoresAtuais,
        PaginaFiscal,
        CorteReliga,
        DataHora,
        NomeUnidade,
        MemoriaMassa,
        ResetRegistradores,
        ParametrosQEE,
        InicioMemoriaMassaQEE,
        MemoriaMassaQEE,
        IniciarCargaDePrograma,
        IniciarConfiguracao,
        IniciaModoPrateleira,
        MonitoramentoDeTransformador,
        ModoTeste,
        IntervaloMM,
        InicioMemoriaMassaSM,
        MemoriaMassaSM,
        ConfiguracaoNIC,
        PredefinicaoNIC,
        MenuCalibracao,
        FIM
    }

    public enum TipoMedidor {
        ABSOLUTO,
        EASY_TRAFO,
        EASY_VER,
        EASY_VOLT,
        MAX_EXT_UNQ
    }

    private Dialog dialogModoTeste = null;
    private int mByteCount = 0;
    private ByteArrayOutputStream mBytesReceived;
    private ArrayList<byte[]> mRespostaComposta = new ArrayList<>();
    private boolean deveLerTudo = false;
    private boolean leuMedidoresIndividuais = false;
    private boolean leuEasyTrafo = false;
    private TipoMedidor tipo_medidor = TipoMedidor.ABSOLUTO;
    private boolean medidorSemRele = false;
    private TipoOperacao funcaoEmExecucao = TipoOperacao.RegistradoresAtuais;
    private Handler mTimeoutHandler;
    private Runnable mMedidoresIndividuaisRunnable;
    private String dataMedidor = "xx/xx/xxxx xx:xx:xx";
    private String versaoMedidor = "00.00";
    private String versaoFWModCom = "00";
    private String revisaoFWModCom = "00";
    private String versaoModCom = "00.00";
    private byte[] iuMedidor = new byte[]{};
    private byte[] android_id = new byte[]{};
    private String modeloMultiponto = "";
    private byte tipoModComAux = 0;
    private String tipoModCom = "";
    private String tipoModulo = "";
    private String estadoModulo = "";
    private String sinalModulo = "";
    private String numMedidor = "";
    private String mNsMedidor = "";
    private byte MetodoNumeroSerial = 0;
    private MedidorAbsoluto medidorSelecionado;
    private RespostaAbsoluto.LeituraDados51 dadosAbnt51;
    private RespostaAbsoluto.LeituraDados52 processa52;
    private RespostaAbsoluto.LeituraCabecalhoQEE dadosCabecalhoQEE;
    private RespostaAbsoluto.LeituraCabecalhoMMSM dadosCabecalhoMMSM;
    private RespostaAbsoluto.LeituraCabecalhoMMSMBloco1 dadosCabecalhoMMSMBloco1;
    private RespostaAbsoluto.LeituraQEE processaQEE;
    private RespostaAbsoluto.LeituraQEESM2 processaQEESM2;
    private RespostaAbsoluto.LeituraQEE processaMMSM;
    private ComandoAbsoluto.EB92 eb92;
    private byte[] mRespostaQEE;
    private byte[] mRespostaMMSM;
    private int AuxPrateleira = 0;

    private boolean easytrafol = false;


    private byte mRetentativas = 0;
    private int codigoCanal = 0;

    private String strSMFiscal = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(Consts.EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(Consts.EXTRAS_DEVICE_ADDRESS);
        strSMFiscal = intent.getStringExtra(Consts.EXTRAS_IS_SMFISCAL);

        mHandler = new Handler(

        );
        findViewById(R.id.ad_imgbtn_openFileManager).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), FileManagerLeiturasActivity.class));
            }
        });
        mRefreshStatus = findViewById(R.id.atualizar_dados);
        mRefreshStatus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRefreshStatus.getAnimation() == null) {
                    animateRefresh();
                    if (tipo_medidor == TipoMedidor.ABSOLUTO) {
                        if (medidores != null && medidores.size() > 0)
                            enviarProximoStatusMedidor("99999999");
                    } else {
                        enviarLeituraMedidoresIndividuais();
                    }
                }
            }
        });

        mTimeoutHandler = new Handler();

        findViewById(R.id.fechar_medidor).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothLeService.disconnect();
                //mStarMeasureService.g
                onBackPressed();
            }
        });

        mRecyclerView = findViewById(R.id.meter_list);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        if (mDeviceName.startsWith("ET01")||mDeviceName.startsWith("ET1")||mDeviceName.startsWith("ET2")) {
            tipo_medidor = TipoMedidor.EASY_TRAFO;
        } else if (mDeviceName.startsWith("ET3")) {
            tipo_medidor = TipoMedidor.EASY_VOLT;
        } else if (mDeviceName.startsWith("EV")) {
            tipo_medidor = TipoMedidor.EASY_VER;
        } else if(mDeviceName.startsWith("MAX") || mDeviceName.startsWith("EXT") || mDeviceName.startsWith("UNQ")){
            tipo_medidor = TipoMedidor.MAX_EXT_UNQ;
        }

        if (tipo_medidor != TipoMedidor.ABSOLUTO) {
            CardView mainLayout = this.findViewById(R.id.unidades_consumidoras_header);
            mainLayout.setVisibility(LinearLayout.GONE);
        }



        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    private boolean sendData(byte[] data) {
        BluetoothGattCharacteristic writeCharacteristic;
        if (isNewModule) {
            writeCharacteristic = mStarMeasureService
                    .getCharacteristic(UUID.fromString(Consts.BLE_NEW_WRITE_CHARACTERISTIC));
        } else {
            writeCharacteristic = mStarMeasureService
                    .getCharacteristic(UUID.fromString(Consts.BLE_OLD_WRITE_CHARACTERISTIC));
        }
        if (writeCharacteristic == null)
            return false;
        writeCharacteristic.setValue(data);
        mBluetoothLeService.writeCharacteristic(writeCharacteristic);
        mByteCount = 0;
        mBytesReceived = new ByteArrayOutputStream();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (rodandoCarga) {
            enviarCargaDePrograma();
        } else {

            if (tipo_medidor == TipoMedidor.ABSOLUTO) {
                showProgressBar("Lendo medidores individuais");
            } else {
                if (tipo_medidor == TipoMedidor.EASY_TRAFO) {
                    showProgressBar("Lendo easyTrafo");
                } else if(tipo_medidor == TipoMedidor.EASY_VOLT) {
                    showProgressBar("Lendo easyVolt");
                } else if(tipo_medidor == TipoMedidor.EASY_VER) {
                    showProgressBar("Lendo easyVer");
                } else if(tipo_medidor == TipoMedidor.MAX_EXT_UNQ){
                    showProgressBar("Lendo Max-Ext-Unq");
                }
            }

            leuMedidoresIndividuais = false;
            leuEasyTrafo = false;

            enviarLeituraMedidoresIndividuais();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (NIC == 1) {
            funcaoEmExecucao = TipoOperacao.ConfiguracaoNIC;
            NICPre();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTimeoutHandler.removeCallbacksAndMessages(null);
        mHandler.removeCallbacksAndMessages(null);
        contaTentativasContador = 0;
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "AGORA FINALIZOU A CONEXAO");
        mBluetoothLeService.disconnect();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }

            mStarMeasureService = null;
            for (BluetoothGattService gattService : mBluetoothLeService.getSupportedGattServices()) {
                if (gattService.getUuid().toString().equals(Consts.BLE_OLD_SERVICE)) {
                    mStarMeasureService = gattService;
                    break;
                } else if (gattService.getUuid().toString().equals(Consts.BLE_NEW_SERVICE)) {
                    mStarMeasureService = gattService;
                    isNewModule = true;
                    break;
                }
            }

            // Automatically connects to the device upon successful start-up initialization.
            if((mBluetoothLeService != null)&&(mDeviceAddress != null)){
                mBluetoothLeService.connect(mDeviceAddress);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long tempoAtual;
            long tempoEnvioCfgRemota;
            long difTempo;
            final String action = intent.getAction();
            Log.d(TAG, "ACTIVE DEVICE RECEIVER " + action);
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                Log.d(TAG, "***************************** LEITURA ATENCAO");

                BluetoothGattCharacteristic mNotifyCharacteristic;
                if (isNewModule) {
                    mNotifyCharacteristic = mStarMeasureService.getCharacteristic(UUID.fromString(Consts.BLE_NEW_NOTIFY_CHARACTERISTIC));
                } else {
                    mNotifyCharacteristic = mStarMeasureService.getCharacteristic(UUID.fromString(Consts.BLE_OLD_NOTIFY_CHARACTERISTIC));
                }
                if (mNotifyCharacteristic != null) {
                    mBluetoothLeService.setCharacteristicNotification(mNotifyCharacteristic, true);
                }
                //mRefreshStatus.callOnClick();*/
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mBluetoothLeService.connect(mDeviceAddress);
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                final byte[] data = intent.getByteArrayExtra(BluetoothLeService.EXTRA_BYTES);
                try {
                    mBytesReceived.write(data);
                    mByteCount += data.length;
                    Log.d(TAG, "REC->" + Util.ByteArrayToHexString(mBytesReceived.toByteArray()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (mByteCount >= 258 || rodandoCarga) {
                    processData();
                    mByteCount = 0;
                    mBytesReceived.reset();
                    if((bAlterandoConfigRemota)&&(!brespostaEB17))
                    {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        enviaComandoEB17((byte) 0, MetodoNumeroSerial);
                        iContadorLeituraEB17Enviado++;
                        if(iContadorLeituraEB17Enviado > 180){
                            iContadorLeituraEB17Enviado = 0;
                            bAlterandoConfigRemota = false;
                            Toast.makeText(context, "Erro ao alterar cfg da remota.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }
    };

    private void showTimeOutMM() {
        if (mMMRunnable == null) {
            mMMRunnable = () -> {
                progressDialog.hide();
                progressDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(DeviceActivity.this,
                        android.R.style.Theme_Material_Dialog_Alert);
                builder.setTitle("Medidor Desconectado")
                        .setMessage("Medidor não conectado!")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("OK", (dialog, which) -> {
                            onBackPressed();
                        })
                        .show();
            };
        }
        mHandler.removeCallbacksAndMessages(null);
        mHandler.postDelayed(mMMRunnable, TIMEOUT_MM);
    }

    private void showProgressBar(String mensagem) {
        mLeituraMedidorRunnable = new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(DeviceActivity.this,
                        android.R.style.Theme_Material_Dialog_Alert);
                builder.setTitle("Medidor Desconectado")
                        .setMessage("Medidor não conectado!")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onBackPressed();
                            }
                        })
                        .show();
            }
        };

        mHandler.postDelayed(mLeituraMedidorRunnable, TIMEOUT);

        progressDialog = AlertBuilder.createProgressDialog(this, mensagem);
        progressDialog.show();
    }

    private void enviarLeituraMedidoresIndividuais() {
        new Handler().post(this::enviarAbnt21);
        if (tipo_medidor == TipoMedidor.ABSOLUTO) {
            mMedidoresIndividuaisRunnable = () -> {
                if (!leuMedidoresIndividuais) {
                    leuMedidoresIndividuais = true;
                    progressDialog.dismiss();
                    mHandler.removeCallbacksAndMessages(null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(DeviceActivity.this,
                            android.R.style.Theme_Material_Dialog_Alert);
                    builder.setTitle("Erro")
                            .setMessage("Timeout na leitura dos medidores individuais")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mBluetoothLeService.disconnect();
                                    onBackPressed();
                                }
                            })
                            .show();
                }
            };
            mTimeoutHandler.postDelayed(mMedidoresIndividuaisRunnable, 30000);
        }
    }

    //private void enviarAB03(int metodo){
    //    Log.i("enviando","Enviando AB03");
    //    enviarComando(mAB03.build(metodo));
    //}

    private void enviarAB05(int metodo){
        Log.i("enviando","Enviando AB05");
        enviarComando(mAB05.build(metodo));
    }

    private void enviarAB14(int metodo){
        Log.i("enviando", "Enviando AB14");
        enviarComando(mAB14.build(metodo));
    }

    private void enviarAB08() {
        Log.i("enviando", "Enviando AB08");
        enviarComandoComposto(new ComandoAbsoluto.AB08().build(MetodoNumeroSerial));
    }

    private void processarAB08(RespostaAbsoluto respostaAbsoluto) {
        Log.i("processarAB08", "Processando AB08");
        byte[] data = respostaAbsoluto.getData();
        mTimeoutHandler.removeCallbacksAndMessages(null);
        mHandler.removeCallbacksAndMessages(null);
        progressDialog.dismiss();
        if (verificaErroAB08(data[7])) {
            if (mCarga != null) {
                android_id = getDeviceId();
                mCargaDePrograma = new ComandoAbsoluto.CargaDePrograma(iuMedidor, android_id);
                byte[] dadosBrutos = CargaController.getBytesCargaDePrograma(mCarga);
                if (dadosBrutos != null && dadosBrutos.length > 15) {
                    mCarga.setDadosBrutos(dadosBrutos);
                    byte[] size = new byte[]{dadosBrutos[14], dadosBrutos[13]};
                    short i = ByteBuffer.wrap(size).getShort();
                    Log.i("incredible", String.valueOf(i));
                    mCarga.setMaxSize(i);
                    ArrayList<byte[]> bPack = CargaController.dividePacks(mCarga);
                    mCarga.setDataToSend(bPack);
                    enviarCargaDePrograma();
                } else {
                    Log.e("processarAB08", "Dados brutos nulo");
                }
            }
        }
    }

    private void enviarCargaDePrograma() {
        Log.i("carga", "Enviando carga de programa");
        rodandoCarga = true;
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
        mTimeoutHandler.postDelayed(() -> {
            Log.e("error", "TIME OUT CARGA DE PROGRAMA");
            AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            builder.setTitle("Aviso");
            builder.setMessage("Falha ao enviar carga de programa.\nConecte-se novamente.");
            builder.setCancelable(false);
            builder.setNegativeButton("Entendi", (p1, p2) -> {
                mBluetoothLeService.disconnect();
                finish();
            });
            builder.create();
            builder.show();
            mTimeoutHandler.removeCallbacksAndMessages(null);
            mHandler.removeCallbacksAndMessages(null);
            rodandoCarga = false;
        }, 10000);
        if (mCargaDePrograma == null) {
            Log.i("carga", "Instanciando carga de programa");
            mCargaDePrograma = new ComandoAbsoluto.CargaDePrograma(iuMedidor, android_id);
        }
        byte[] dataToSend = null;
        try {
            if (mCargaDePrograma.getPacoteAtual() == 0) {
                Log.i("carga", "Pacote atual 0 - enviando primeiro comando");
                mCargaDePrograma.setDados(mCarga.getDataToSend().get(0));
                mCargaDePrograma.setMensagemFinal(1);
                mCargaDePrograma.setComandoAtual(ComandoAbsoluto.CargaDePrograma.iniciaCarga);
                progressDialog.setMessage("Iniciando carga de programa");
            } else {
                Log.i("carga", "Pacote atual " + mCargaDePrograma.getPacoteAtual() + " - comando de sequencia");
                byte[] dados = mCarga.getDataToSend().get(mCargaDePrograma.getPacoteAtual());
                mCargaDePrograma.setComandoAtual(ComandoAbsoluto.CargaDePrograma.transferenciaDeCarga);
                if (mCargaDePrograma.getPacoteAtual() == (mCarga.getDataToSend().size() - 1)) {
                    mCargaDePrograma.setMensagemFinal(1);
                } else {
                    mCargaDePrograma.setMensagemFinal(0);
                }
                mCargaDePrograma.setDados(dados);
                progressDialog.setMessage("Enviando pacote " + mCargaDePrograma.getPacoteAtual() + " de " + (mCarga.getDataToSend().size() - 1));
            }

            dataToSend = mCargaDePrograma.build();
        } catch (IOException e) {
            e.printStackTrace();
            mTimeoutHandler.removeCallbacksAndMessages(null);
            mHandler.removeCallbacksAndMessages(null);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            Toast.makeText(mBluetoothLeService, "ERRO: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (dataToSend != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
                Log.i("comandoenviar", Util.ByteArrayToHexString(dataToSend));
                enviarComando(dataToSend);
            }
        }
    }

    private void processarCargaDePrograma(RespostaAbsoluto respostaAbsoluto) {
        Log.i("processaCarga", "Recebeu dados");
        mTimeoutHandler.removeCallbacksAndMessages(null);
        mHandler.removeCallbacksAndMessages(null);
        byte[] data = respostaAbsoluto.getData();
        Log.i("processaCarga", "DADOS-> " + Util.ByteArrayToHexString(data));
        byte[] cabecalho = new byte[]{data[0], data[1]};
        if ((cabecalho[1] & 0x01) == 1) {
            Log.e("processaCarga", "Erro | enviando mesmo comando");
            int sequenciadorAtual = mCargaDePrograma.getSequenciador() + 1;
            mCargaDePrograma.setSequenciador(sequenciadorAtual);
            enviarCargaDePrograma();
            return;
        }

        // Não utilizado ainda
        byte[] ocorrencias = new byte[]{data[2], data[3], data[4], data[5]};

        byte[] dados;

        byte retorno = data[6];
        if (retorno == 0x02) {
            dados = new byte[]{data[7]};
            byte erro = dados[0];
            String errorMsg = null;
            if (erro == 0x20) {
                errorMsg = "Tipo de equipamento inválido";
            } else if (erro == 0x21) {
                errorMsg = "Versão/Revisão de software não suportada";
            } else if (erro == 0x22) {
                errorMsg = "Tamanho do programa inválido";
            } else if (erro == 0x23) {
                errorMsg = "Offset do ponto de entrada do programa inválido";
            } else if (erro == 0x24) {
                errorMsg = "Tamanho do pacote de transferência não suportado";
            } else if (erro == 0x25) {
                if (contaTentativasContador < 5) {
                    contaTentativasContador++;
                    mCargaDePrograma.atualizarSequenciador();
                    mCargaDePrograma.atualizarContadorDePacote();

                    if (mCarga.getDataToSend().size() != mCargaDePrograma.getPacoteAtual()) {
                        enviarCargaDePrograma();
                    } else {
                        mBluetoothLeService.disconnect();
                        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                        builder.setTitle("Aviso");
                        builder.setMessage("Carga de programa finalizada. O medidor está reiniciando.");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Entendi", (p1, p2) -> finish());
                        builder.create();
                        builder.show();
                    }
                } else {
                    errorMsg = "Contador do pacote de transferência inválido";
                }
            } else if (erro == 0x26) {
                errorMsg = "Tentativa de transferência de carga sem inicialização";
            } else if (erro == 0x27) {
                errorMsg = "Erro na finalização da carga de programa";
            } else {
                errorMsg = "Erro desconhecido";
            }

            if (errorMsg != null) {
                Log.e("processaCarga", errorMsg);

                AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                builder.setTitle("Erro - Carga de Programa");
                builder.setMessage(errorMsg);
                builder.setCancelable(false);
                builder.setPositiveButton("Entendi", (p1, p2) -> {
                    mBluetoothLeService.disconnect();
                    finish();
                });
                builder.create();
                builder.show();
            }

        } else if (retorno == 0x0C) {
            dados = new byte[]{data[10], data[9], data[8], data[7]};
            mCargaDePrograma.setSequenciador(ByteBuffer.wrap(dados).getInt());
            Log.i("processaCarga", "Novo sequenciador necessário: " + ByteBuffer.wrap(dados).getInt());
            enviarCargaDePrograma();
        } else if (retorno == 0x00) {
            mCargaDePrograma.atualizarSequenciador();
            mCargaDePrograma.atualizarContadorDePacote();

            if (mCarga.getDataToSend().size() != mCargaDePrograma.getPacoteAtual()) {
                enviarCargaDePrograma();
            } else {
                mBluetoothLeService.disconnect();
                AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                builder.setTitle("Aviso");
                builder.setMessage("Carga de programa finalizada. O medidor está reiniciando.");
                builder.setCancelable(false);
                builder.setPositiveButton("Entendi", (p1, p2) -> finish());
                builder.create();
                builder.show();
            }
        } else if (retorno == 0x01) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            builder.setTitle("Erro - Carga de Programa");
            builder.setMessage("Comando não reconhecido pelo medidor");
            builder.setCancelable(false);
            builder.setPositiveButton("Entendi", (p1, p2) -> {
                mBluetoothLeService.disconnect();
                finish();
            });
            builder.create();
            builder.show();
        } else {
            Log.e("processaCarga", "ERRO");
        }
    }

    private boolean verificaErroAB08(byte data) {
        if (data == 0) {
            return true;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        builder.setTitle("Aviso");
        if (data == 1) {
            builder.setMessage("Interface não suportada");
        } else if (data == 2) {
            builder.setMessage("Protocolo não suportado");
        }
        builder.setNegativeButton("Entendi", null);
        builder.create();
        builder.show();
        return false;
    }

    @NotNull
    private byte[] getDeviceId() {
        @SuppressLint("HardwareIds")
        String deviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.i("getBluetoothMac", deviceID);
        byte[] list = deviceID.getBytes();

        if (list != null && list.length > 0) {
            if (list.length > 4) {
                return new byte[]{list[0], list[1], list[2], list[3], (byte) 0x91};
            } else if (list.length == 3) {
                return new byte[]{list[0], list[1], list[2], 0x00, (byte) 0x91};
            } else if (list.length == 2) {
                return new byte[]{list[0], list[1], 0x00, 0x00, (byte) 0x91};
            } else if (list.length == 1) {
                return new byte[]{list[0], 0x00, 0x00, 0x00, (byte) 0x91};
            }
        }

        return new byte[]{0x00, 0x00, 0x00, 0x00, (byte) 0x91};
    }

    private void enviarLeituraConfiguracao(TipoMedidor tipo_medidor) {
        switch(tipo_medidor){
            case ABSOLUTO:
            case MAX_EXT_UNQ:
                enviarComandoComposto(
                        new ComandoAbsoluto.LeituraConfiguracaoMedidor()
                                .build(true)
                );
                break;
            case EASY_TRAFO:
            case EASY_VER:
            case EASY_VOLT:
                enviarComandoComposto(
                        new ComandoAbsoluto.LeituraConfiguracaoMedidor()
                                .build(false)
                );
                break;
        }
    }

    private void enviarLeituraParametrosHospedeiro() {
        enviarComandoComposto(
                new ComandoAbsoluto
                        .LeituraParametrosHospedeiro()
                        .build()
        );
    }

    private void enviarAbnt21() {
        Log.d(TAG, "enviarAbnt21");
        leuEasyTrafo = false;
        mTimeoutHandler.postDelayed(() -> {
            mRetentativas++;
            if (mRetentativas > 5) {
                mTimeoutHandler.removeCallbacksAndMessages(null);
                mRefreshStatus.clearAnimation();
                progressDialog.dismiss();
                mHandler.removeCallbacksAndMessages(null);
                AlertDialog.Builder builder = new AlertDialog.Builder(DeviceActivity.this,
                        android.R.style.Theme_Material_Dialog_Alert);
                builder.setTitle("Erro")
                        .setMessage("Timeout na leitura do Medidor")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mBluetoothLeService.disconnect();
                                onBackPressed();
                            }
                        })
                        .show();
            } else {
                byte[] data = new ComandoAbsoluto
                        .ABNT21()
                        .comMedidorNumero("000000", MetodoNumeroSerial)
                        .build((tipo_medidor == TipoMedidor.ABSOLUTO), MetodoNumeroSerial);
                enviarComando(data);
                Log.d(TAG, "RETRY ET: " + Util.ByteArrayToHexString(data));
            }
        }, 3000);


        byte[] data = new ComandoAbsoluto
                .ABNT21()
                .comMedidorNumero("000000", MetodoNumeroSerial)
                .build((tipo_medidor == TipoMedidor.ABSOLUTO), MetodoNumeroSerial);
        enviarComando(data);
        Log.d(TAG, "Enviando ABNT21: " + Util.ByteArrayToHexString(data));
        mRetentativas = 1;
    }

    private void enviarLeitura87() {
        Log.d(TAG, "enviarAbnt87 EasyTrafo/Ver");

        byte[] data = new ComandoAbsoluto
                .ABNT87()
                .comMedidorNumero("000000")
                .build((tipo_medidor == TipoMedidor.ABSOLUTO));
        enviarComando(data);
        Log.d(TAG, "Enviando ET: " + Util.ByteArrayToHexString(data));
    }

    private void enviarLeituraStatusMedidores(String numeroMedidor, int metodo) {
        mTimeoutHandler.removeCallbacksAndMessages(null);
        mTimeoutHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (deveLerTudo) {
                    deveLerTudo = false;
                    mRefreshStatus.clearAnimation();
                    showErrorMessage(getString(R.string.text_timeout_leitura));
                }
            }
        }, 3000);
        enviarComando(
                new ComandoAbsoluto.AB06AlteracaoCorteReligamento()
                        .comMedidorNumero(numeroMedidor, metodo)
                        .efetuaLeitura()
                        .build((tipo_medidor == TipoMedidor.ABSOLUTO), metodo)
        );
    }

    private void enviarAberturaSessao(String numeroMedidor, int metodo) {
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
                                .build((tipo_medidor == TipoMedidor.ABSOLUTO), metodo);
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

    private void processData() {

        final byte[] data = mBytesReceived.toByteArray();
        RespostaAbsoluto respostaAbsoluto = new RespostaAbsoluto(data);
        if (respostaAbsoluto.isOcorrencia()) {
            processarRespostaOcorrencia(respostaAbsoluto);
            leuMedidoresIndividuais = false;
        } else if (respostaAbsoluto.isLeituraConfiguracaoMedidor()) {
            if (tipo_medidor == TipoMedidor.ABSOLUTO) {
                processarRespostaConfiguracaoMedidorAbsoluto(respostaAbsoluto);
            } else {
                processarRespostaConfiguracaoMedidorEasy(respostaAbsoluto);
            }
        } else if (respostaAbsoluto.isLeituraParametrosHospedeiro()) {
            processarRespostaLeituraParametrosHospedeiro(respostaAbsoluto);
        } else if(respostaAbsoluto.isAB14()){
            processarRespostaLeituraConfigParamModCom(respostaAbsoluto);
        } else if (respostaAbsoluto.isLeituraStatusMedidor()) {
            processarRespostaLeituraStatusMedidor(respostaAbsoluto);
        } else if (respostaAbsoluto.isCorteReligamento()) {
            processarRespostaCorteReligamento(respostaAbsoluto);
        } else if (respostaAbsoluto.isAberturaSessao()) {
            processarRespostaAberturaSessao(data[7], respostaAbsoluto);
        } else if (respostaAbsoluto.isAbnt21()) {
            if (tipo_medidor == TipoMedidor.ABSOLUTO) {
                processarRespostaAbnt21Absoluto(respostaAbsoluto);
            } else {
                processarRespostaAbnt21(respostaAbsoluto);
            }
        } else if (respostaAbsoluto.isAbnt87()) {
            processarRespostaAbnt87(respostaAbsoluto);
            if (TipoOperacao.NomeUnidade == funcaoEmExecucao) {
                Toast.makeText(this, "Nome do ponto alterado com sucesso.", Toast.LENGTH_SHORT).show();
                funcaoEmExecucao = TipoOperacao.FIM;
                mRefreshStatus.callOnClick();
            }
        } else if (respostaAbsoluto.isAbnt73()) {
            processarRespostaAbnt73(respostaAbsoluto);
            if (funcaoEmExecucao == TipoOperacao.IntervaloMM) {
                Toast.makeText(this, "Intervalo da MM alterado com sucesso.", Toast.LENGTH_SHORT).show();
                funcaoEmExecucao = TipoOperacao.FIM;
                mRefreshStatus.callOnClick();
            }
        } else if (respostaAbsoluto.isRespostaOcorrencias()) {
            mRefreshStatus.clearAnimation();
            leuMedidoresIndividuais = false;
            enviarLeituraMedidoresIndividuais();
        } else if (respostaAbsoluto.isTensaoReferencia()) {
            enviarTipoLigacao(medidorSelecionado.numero, MetodoNumeroSerial);
        } else if (respostaAbsoluto.isLeituraGenerica()) {
            Toast.makeText(this, "QEE parametrizado com sucesso.", Toast.LENGTH_SHORT).show();
        } else if (respostaAbsoluto.isRespostaData()) {
            ajustaDataHora(false, medidorSelecionado);
        } else if (respostaAbsoluto.isRespostaHora()) {
            Toast.makeText(this, "Data e hora ajustada com sucesso.", Toast.LENGTH_SHORT).show();
            funcaoEmExecucao = TipoOperacao.FIM;
            mRefreshStatus.callOnClick();
        } else if (respostaAbsoluto.isRespostaReset()) {
            Toast.makeText(this, "Reset dos registradores realizado com sucesso.", Toast.LENGTH_SHORT).show();
        } else if (respostaAbsoluto.isRespostaLeituraParametros()) {
            processarRespostaAbnt51(respostaAbsoluto);
        } else if (respostaAbsoluto.isRespostaMemoriaMassaQEE()) {
            if (funcaoEmExecucao == TipoOperacao.ParametrosQEE) {
                enviarQEEParametrizacao(respostaAbsoluto, medidorSelecionado.numero);
            } else if (funcaoEmExecucao == TipoOperacao.MonitoramentoDeTransformador) {
                processarRespostaEB11ParaMonitoramento(respostaAbsoluto);
            } else {
                processarRespostaEB11(respostaAbsoluto);
            }
        } else if (respostaAbsoluto.isRespostaMemoriaMassaSM()) {
            processarRespostaAB52(respostaAbsoluto);
        } else if (respostaAbsoluto.isRespostaMemoriaMassa()) {
            processarRespostaAbnt52(respostaAbsoluto);
        } else if (respostaAbsoluto.isAb08()) {
            processarAB08(respostaAbsoluto);
        } else if (rodandoCarga) {
            processarCargaDePrograma(respostaAbsoluto);
        } else if (respostaAbsoluto.isEB17()) {
            if ((funcaoEmExecucao == TipoOperacao.ConfiguracaoNIC)||(funcaoEmExecucao == TipoOperacao.IniciaModoPrateleira)) {
                brespostaEB17 = processaEB17(respostaAbsoluto);
            }
        } else if (respostaAbsoluto.isEB90()) {
            processaEB90(respostaAbsoluto);
        } else if (respostaAbsoluto.isEB92()) {
            processaEB92(respostaAbsoluto);
        } else if (respostaAbsoluto.isAB03()) {
            brespostaAB03 = processaAB03(respostaAbsoluto);
        } else if (respostaAbsoluto.isAB05()) {
            brespostaAB05 = processaAB05(respostaAbsoluto);
        }
        else {
            processarRespostaNaoTratada(data);
        }
    }

    private void enviaEB17() {
        Log.i("mEB17", mEB17.toString());
        enviarComando(mEB17.build());
        rodandoEB17 = true;
        if(AuxPrateleira == 0){
            dialogConfiguracaoNIC.findViewById(R.id.configuracaonic_loading).setVisibility(VISIBLE);
            dialogConfiguracaoNIC.findViewById(R.id.configuracaonic_enviar_alteracao).setEnabled(false);
        }
    }

    private void enviaEB90() {
        Log.i("mEB90", mEB90.toString());
        enviarComando(mEB90.build());
        rodandoEB90 = true;
        dialogMonitoramento.findViewById(R.id.monitoramento_loading).setVisibility(VISIBLE);
        dialogMonitoramento.findViewById(R.id.monitoramento_btn_luz).setEnabled(false);
        dialogMonitoramento.findViewById(R.id.monitoramento_btn_forca_1).setEnabled(false);
        dialogMonitoramento.findViewById(R.id.monitoramento_btn_forca_2).setEnabled(false);
        dialogMonitoramento.findViewById(R.id.monitoramento_enviar_alteracao).setEnabled(false);
    }

    private Boolean processaAB03(RespostaAbsoluto respostaAbsoluto){
        ComandoAbsoluto.AB03 mAB03 = new ComandoAbsoluto.AB03();


        RespostaAbsoluto.LeituraAB03 mLeitura = respostaAbsoluto.interpretaAB03();

        Log.i("pcAB03", mLeitura.toString());

        return false;
    }

    private Boolean processaAB05(RespostaAbsoluto respostaAbsoluto){
        ComandoAbsoluto.AB05 mAB05 = new ComandoAbsoluto.AB05();


        RespostaAbsoluto.LeituraAB05 mLeitura = respostaAbsoluto.interpretaAB05();

        iModoEspecial = mLeitura.ModoEspecial;
        iCalibracao = mLeitura.Calibracao;

        Log.i("pcAB05", mLeitura.toString());

        return false;
    }

    private Boolean processaEB17(RespostaAbsoluto respostaAbsoluto) {
        ComandoAbsoluto.EB17 mEB17 = new ComandoAbsoluto.EB17();

        RespostaAbsoluto.LeituraEB17 mLeitura = respostaAbsoluto.interpretaEB17();
        Log.i("pcEB17", mLeitura.toString());
        //if(mEB17 != null)
        //{
        //    mEB17.setLeitura(true);
        //}

        rodandoEB17 = false;

        if(funcaoEmExecucao == TipoOperacao.ConfiguracaoNIC){
            dialogConfiguracaoNIC.findViewById(R.id.configuracaonic_loading).setVisibility(INVISIBLE);
            dialogConfiguracaoNIC.findViewById(R.id.configuracaonic_enviar_alteracao).setEnabled(true);
        }


        if(mLeitura.isLeitura){
            if(!bAlterandoConfigRemota) {
                Toast.makeText(this, "Dados recebidos da leitura...", Toast.LENGTH_SHORT).show();
            }

            if(mLeitura.ModoPrateleira == 0){
                bUltimoEstadoModoPrateleira = true;
            }
            else
            {
                bUltimoEstadoModoPrateleira = false;
            }
        }
        else
        {
            Toast.makeText(this, "Dados recebidos do comando de alteração...", Toast.LENGTH_SHORT).show();
        }

        if((((mLeitura.ValeAlteracao == 0) && (mLeitura.ValeAlteracao2 == 0)) || (!bAlterandoConfigRemota)) && (mLeitura.isLeitura == true)){
            if(bAlterandoConfigRemota){
                bAlterandoConfigRemota = false;
                iContadorLeituraEB17Enviado = 0;
                Toast.makeText(this,"Alteração concluída", Toast.LENGTH_SHORT).show();
            }

            if(funcaoEmExecucao == TipoOperacao.ConfiguracaoNIC){
                atualizaDialogConfiguracaoNIC(mLeitura);

                dialogConfiguracaoNIC.findViewById(R.id.cb_configuracaonic_telefone1_check).setEnabled(true);
                dialogConfiguracaoNIC.findViewById(R.id.cb_configuracaonic_telefone2_check).setEnabled(true);
                dialogConfiguracaoNIC.findViewById(R.id.cb_configuracaonic_telefone3_check).setEnabled(true);
                dialogConfiguracaoNIC.findViewById(R.id.cb_configuracaonic_telefone4_check).setEnabled(true);
                dialogConfiguracaoNIC.findViewById(R.id.cb_Telefone_Keep_Alive).setEnabled(true);
            } else{
                sTelefone1 = mLeitura.Telefone1;
                sTelefone2 = mLeitura.Telefone2;
                sTelefone3 = mLeitura.Telefone3;
                sTelefone4 = mLeitura.Telefone4;
                sTelefoneKeepAlive = mLeitura.TelefoneKeepAlive;
                iEventos = mLeitura.Eventos;
                iCiclos = mLeitura.Ciclos;
                iModoPrateleira = mLeitura.ModoPrateleira;
                iRepeticoes = mLeitura.RepeticoesEtapa1;
                iIntervalo1 = mLeitura.Intervalo1;
                iIntervalo2 = mLeitura.Intervalo2;
                iValidade = mLeitura.Validade;
                iFrequenciaKeepAlive = mLeitura.FrequenciaKeepAlive;
            }
            return true;
        }
        return false;
    }

    private void processaEB90(RespostaAbsoluto respostaAbsoluto) {
        RespostaAbsoluto.LeituraEB90 mLeitura = respostaAbsoluto.interpretaEB90();
        Log.i("pcEB90", mLeitura.toString());
        mEB90.setLeitura(true);

        rodandoEB90 = false;
        dialogMonitoramento.findViewById(R.id.monitoramento_loading).setVisibility(INVISIBLE);
        dialogMonitoramento.findViewById(R.id.monitoramento_btn_luz).setEnabled(true);
        dialogMonitoramento.findViewById(R.id.monitoramento_btn_forca_1).setEnabled(true);
        dialogMonitoramento.findViewById(R.id.monitoramento_btn_forca_2).setEnabled(true);
        dialogMonitoramento.findViewById(R.id.monitoramento_enviar_alteracao).setEnabled(true);

        Spinner spPotenciaNominal = dialogMonitoramento.findViewById(R.id.monitoramento_potencia_nominal);

        switch (mLeitura.transformadorLido) {
            case 0:
                dialogMonitoramento.findViewById(R.id.monitoramento_btn_luz).setEnabled(false);
                break;
            case 1:
                dialogMonitoramento.findViewById(R.id.monitoramento_btn_forca_1).setEnabled(false);
                break;
            case 2:
                dialogMonitoramento.findViewById(R.id.monitoramento_btn_forca_2).setEnabled(false);
                break;
        }
        switch (mLeitura.potenciaNominal) {
            case 250:
            case 1125:
                spPotenciaNominal.setSelection(0);
                break;
            case 375:
            case 1500:
                spPotenciaNominal.setSelection(1);
                break;
            case 500:
            case 2250:
                spPotenciaNominal.setSelection(2);
                break;
            case 750:
                spPotenciaNominal.setSelection(3);
                break;
            case 1000:
                spPotenciaNominal.setSelection(4);
                break;

        }

        atualizaDialogMonitoramento(mLeitura);
    }

    private void atualizaDialogMonitoramento(RespostaAbsoluto.LeituraEB90 mLeitura) {
        mEB90.setSobretensaoA(mLeitura.nivelSobretensaoFaseA);
        mEB90.setSobretensaoB(mLeitura.nivelSobretensaoFaseB);
        mEB90.setSobretensaoC(mLeitura.nivelSobretensaoFaseC);
        mEB90.setSubtensaoA(mLeitura.nivelSubtensaoFaseA);
        mEB90.setSubtensaoB(mLeitura.nivelSubtensaoFaseB);
        mEB90.setSubtensaoC(mLeitura.nivelSubtensaoFaseC);

        Switch switchPapelTermoestabilizado = dialogMonitoramento.findViewById(R.id.monitoramento_papel_termoestabilizado);
        TextView tvTemperaturaNivel1 = dialogMonitoramento.findViewById(R.id.monitoramento_temperatura_nivel_1_result);
        TextView tvTemperaturaNivel2 = dialogMonitoramento.findViewById(R.id.monitoramento_temperatura_nivel_2_result);
        TextView tvExpoenteDoOleo = dialogMonitoramento.findViewById(R.id.monitoramento_expoente_do_oleo_result);
        TextView tvExpoenteDoEnrolamento = dialogMonitoramento.findViewById(R.id.monitoramento_expoente_do_enrolamento_result);
        TextView tvConstanteDoTempoDoOleo = dialogMonitoramento.findViewById(R.id.monitoramento_constante_tempo_oleo_result);
        TextView tvConstanteDoTempoDoEnrolamento = dialogMonitoramento.findViewById(R.id.monitoramento_constante_tempo_enrolamento_result);
        TextView tvK11 = dialogMonitoramento.findViewById(R.id.monitoramento_k11_result);
        TextView tvK21 = dialogMonitoramento.findViewById(R.id.monitoramento_k21_result);
        TextView tvK22 = dialogMonitoramento.findViewById(R.id.monitoramento_k22_result);
        TextView tvConstanteDiscretizacao = dialogMonitoramento.findViewById(R.id.monitoramento_constante_discretizacao_result);
        TextView tvRelacaoPerdasCorrenteNominalVazio = dialogMonitoramento.findViewById(R.id.monitoramento_relacao_perdas_corrente_nominal_vazio_result);
        TextView tvTemperaturaPontoMaisQuenteNominal = dialogMonitoramento.findViewById(R.id.monitoramento_temperatura_ponto_mais_quente_nominal_result);
        TextView tvElevacaoTemperaturaTopoOleo = dialogMonitoramento.findViewById(R.id.monitoramento_elevacao_temperatura_topo_oleo_result);
        TextView tvGradienteDiferencaTemperaturasPontoMaisQuenteTopoOleo = dialogMonitoramento.findViewById(R.id.monitoramento_gradiente_diferenca_temperaturas_ponto_mais_quente_topo_oleo_result);
        TextView tvNivel1AlarmeSobretemperaturaDoPontoMaisQuenteDoTransformador = dialogMonitoramento.findViewById(R.id.monitoramento_nivel_1_alarme_sobretemperatura_do_mais_quente_do_transformador_result);
        TextView tvNivel2AlarmeSobretemperaturaDoPontoMaisQuenteDoTransformador = dialogMonitoramento.findViewById(R.id.monitoramento_nivel_2_alarme_sobretemperatura_do_mais_quente_do_transformador_result);
        TextView tvSobrecorrenteFaseA = dialogMonitoramento.findViewById(R.id.monitoramento_nivel_sobrecorrente_fase_A_result);
        TextView tvSobrecorrenteFaseB = dialogMonitoramento.findViewById(R.id.monitoramento_nivel_sobrecorrente_fase_B_result);
        TextView tvSobrecorrenteFaseC = dialogMonitoramento.findViewById(R.id.monitoramento_nivel_sobrecorrente_fase_C_result);
        EditText etSobretensaoFaseA = dialogMonitoramento.findViewById(R.id.monitoramento_nivel_sobretensao_fase_A_result);
        EditText etSobretensaoFaseB = dialogMonitoramento.findViewById(R.id.monitoramento_nivel_sobretensao_fase_B_result);
        EditText etSobretensaoFaseC = dialogMonitoramento.findViewById(R.id.monitoramento_nivel_sobretensao_fase_C_result);
        EditText etSubtensaoFaseA = dialogMonitoramento.findViewById(R.id.monitoramento_nivel_subtensao_fase_A_result);
        EditText etSubtensaoFaseB = dialogMonitoramento.findViewById(R.id.monitoramento_nivel_subtensao_fase_B_result);
        EditText etSubtensaoFaseC = dialogMonitoramento.findViewById(R.id.monitoramento_nivel_subtensao_fase_C_result);

        switchPapelTermoestabilizado.setChecked(mLeitura.temPapeltermoestabilizado);
        tvTemperaturaNivel1.setText(String.valueOf(mLeitura.valorTemperaturaNivel1));
        tvTemperaturaNivel2.setText(String.valueOf(mLeitura.valorTemperaturaNivel2));
        tvExpoenteDoOleo.setText(String.valueOf(mLeitura.expoenteDoOleo));
        tvExpoenteDoEnrolamento.setText(String.valueOf(mLeitura.expoenteDoEnrolamento));
        tvConstanteDoTempoDoOleo.setText(String.valueOf(mLeitura.constanteDeTempoDoOleo));
        tvConstanteDoTempoDoEnrolamento.setText(String.valueOf(mLeitura.constanteDeTempoDoEnrolamento));
        tvK11.setText(String.valueOf(mLeitura.constanteDoModeloTermico11));
        tvK21.setText(String.valueOf(mLeitura.constanteDoModeloTermico21));
        tvK22.setText(String.valueOf(mLeitura.constanteDoModeloTermico22));
        tvConstanteDiscretizacao.setText(String.valueOf(mLeitura.constanteDeDiscretizacao));
        tvRelacaoPerdasCorrenteNominalVazio.setText(String.valueOf(mLeitura.relacaoDePerdasDeCargaNaCorrenteNominalParaPerdasEmVazio));
        tvTemperaturaPontoMaisQuenteNominal.setText(String.valueOf(mLeitura.temperaturaDoPontoMaisQuenteNominal));
        tvElevacaoTemperaturaTopoOleo.setText(String.valueOf(mLeitura.elevacaoDeTemperaturaDoTopoDoOleo));
        tvGradienteDiferencaTemperaturasPontoMaisQuenteTopoOleo.setText(String.valueOf(mLeitura.gradienteObtidoPelaDiferencaEntreTemperaturaDoPontoMaisQuenteEDoTopoDoOleo));
        tvNivel1AlarmeSobretemperaturaDoPontoMaisQuenteDoTransformador.setText(String.valueOf(mLeitura.nivel1DeAlarmeParaSobretemperaturaDoPontoMaisQuenteDoTransformador));
        tvNivel2AlarmeSobretemperaturaDoPontoMaisQuenteDoTransformador.setText(String.valueOf(mLeitura.nivel2DeAlarmeParaSobretemperaturaDoPontoMaisQuenteDoTransformador));
        tvSobrecorrenteFaseA.setText(String.valueOf(mLeitura.nivelSobrecorrenteFaseA));
        tvSobrecorrenteFaseB.setText(String.valueOf(mLeitura.nivelSobrecorrenteFaseB));
        tvSobrecorrenteFaseC.setText(String.valueOf(mLeitura.nivelSobrecorrenteFaseC));
        etSobretensaoFaseA.setHint(String.valueOf(mLeitura.nivelSobretensaoFaseA));
        etSobretensaoFaseA.setText("");
        etSobretensaoFaseB.setHint(String.valueOf(mLeitura.nivelSobretensaoFaseB));
        etSobretensaoFaseB.setText("");
        etSobretensaoFaseC.setHint(String.valueOf(mLeitura.nivelSobretensaoFaseC));
        etSobretensaoFaseC.setText("");
        etSubtensaoFaseA.setHint(String.valueOf(mLeitura.nivelSubtensaoFaseA));
        etSubtensaoFaseA.setText("");
        etSubtensaoFaseB.setHint(String.valueOf(mLeitura.nivelSubtensaoFaseB));
        etSubtensaoFaseB.setText("");
        etSubtensaoFaseC.setHint(String.valueOf(mLeitura.nivelSubtensaoFaseC));
        etSubtensaoFaseC.setText("");
    }

    private void atualizaDialogConfiguracaoNIC(RespostaAbsoluto.LeituraEB17 mLeitura) {

        CheckBox cbTelefone1 = dialogConfiguracaoNIC.findViewById(R.id.cb_configuracaonic_telefone1_check);
        CheckBox cbTelefone2 = dialogConfiguracaoNIC.findViewById(R.id.cb_configuracaonic_telefone2_check);
        CheckBox cbTelefone3 = dialogConfiguracaoNIC.findViewById(R.id.cb_configuracaonic_telefone3_check);
        CheckBox cbTelefone4 = dialogConfiguracaoNIC.findViewById(R.id.cb_configuracaonic_telefone4_check);
        Switch tbPrateleira = dialogConfiguracaoNIC.findViewById(R.id.modo_prateleira);

        EditText titulo = dialogConfiguracaoNIC.findViewById(R.id.titulo);

        EditText etTelefone1 = dialogConfiguracaoNIC.findViewById(R.id.etTelefone1);
        EditText etTelefone2 = dialogConfiguracaoNIC.findViewById(R.id.etTelefone2);
        EditText etTelefone3 = dialogConfiguracaoNIC.findViewById(R.id.etTelefone3);
        EditText etTelefone4 = dialogConfiguracaoNIC.findViewById(R.id.etTelefone4);

        CheckBox cbFaltaEnergia = dialogConfiguracaoNIC.findViewById(R.id.cb_falta_energia);
        CheckBox cbSubtensao = dialogConfiguracaoNIC.findViewById((R.id.cb_subtensao_ciclos));
        CheckBox cbSobretensao = dialogConfiguracaoNIC.findViewById(R.id.cb_sobretensao_ciclos);
        CheckBox cbAberturaTampa = dialogConfiguracaoNIC.findViewById(R.id.cb_abertura_tampa);

        RadioButton rbCicloPadrao = dialogConfiguracaoNIC.findViewById(R.id.rb_ciclo_padrao);
        RadioButton rbCicloEtapa2 = dialogConfiguracaoNIC.findViewById(R.id.rb_etapa2);

        EditText etRepeticoes = dialogConfiguracaoNIC.findViewById(R.id.etRepeticoes);
        EditText etIntervalo1 = dialogConfiguracaoNIC.findViewById(R.id.etIntervalo1);
        EditText etIntervalo2 = dialogConfiguracaoNIC.findViewById(R.id.etIntervalo2);

        CheckBox cbTelefoneKeepAlive = dialogConfiguracaoNIC.findViewById(R.id.cb_Telefone_Keep_Alive);

        EditText etTelefoneKeepAlive = dialogConfiguracaoNIC.findViewById(R.id.etTelefoneKeepAlive);
        EditText etFrequenciaKeepAlive = dialogConfiguracaoNIC.findViewById(R.id.etFrequenciaKeepAlive);

        sTelefone1 = mLeitura.Telefone1;
        sTelefone2 = mLeitura.Telefone2;
        sTelefone3 = mLeitura.Telefone3;
        sTelefone4 = mLeitura.Telefone4;
        sTelefoneKeepAlive = mLeitura.TelefoneKeepAlive;
        iEventos = mLeitura.Eventos;
        iCiclos = mLeitura.Ciclos;
        iModoPrateleira = mLeitura.ModoPrateleira;
        iRepeticoes = mLeitura.RepeticoesEtapa1;
        iIntervalo1 = mLeitura.Intervalo1;
        iIntervalo2 = mLeitura.Intervalo2;
        iValidade = mLeitura.Validade;
        iFrequenciaKeepAlive = mLeitura.FrequenciaKeepAlive;

        if((!mLeitura.Telefone1.contains("00000"))&&(((byte)(mLeitura.Telefone1.length())) > 6))
        {
            etTelefone1.setText(mLeitura.Telefone1);
            cbTelefone1.setChecked(true);
            cbTelefone1.setEnabled(true);
        }
        else
        {
            etTelefone1.setText("");
            cbTelefone1.setChecked(false);
            cbTelefone1.setEnabled(false);
        }

        if((!mLeitura.Telefone2.contains("00000"))&&(((byte)(mLeitura.Telefone2.length())) > 6))
        {
            etTelefone2.setText(mLeitura.Telefone2);
            cbTelefone2.setChecked(true);
            cbTelefone2.setEnabled(true);
        }
        else
        {
            etTelefone2.setText("");
            cbTelefone2.setChecked(false);
            cbTelefone2.setEnabled(false);
        }

        if((!mLeitura.Telefone3.contains("00000"))&&(((byte)(mLeitura.Telefone3.length())) > 6))
        {
            etTelefone3.setText(mLeitura.Telefone3);
            cbTelefone3.setChecked(true);
            cbTelefone3.setEnabled(true);
        }
        else
        {
            etTelefone3.setText("");
            cbTelefone3.setChecked(false);
            cbTelefone3.setEnabled(false);
        }

        if((!mLeitura.Telefone4.contains("00000"))&&(((byte)(mLeitura.Telefone4.length())) > 6))
        {
            etTelefone4.setText(mLeitura.Telefone4);
            cbTelefone4.setChecked(true);
            cbTelefone4.setEnabled(true);
        }
        else
        {
            etTelefone4.setText("");
            cbTelefone4.setChecked(false);
            cbTelefone4.setEnabled(false);
        }

        if(((mLeitura.Eventos >> 0) & 0x01) == 0x01)
        {
            // Bit 0 ligado indicando Falta de Energia
            cbFaltaEnergia.setChecked(true);
        }
        else
        {
            cbFaltaEnergia.setChecked(false);
        }

        if(((mLeitura.Eventos >> 1) & 0x01) == 0x01)
        {
            // Bit 0 ligado indicando Subtensão
            cbSubtensao.setChecked(true);
        }
        else
        {
            cbSubtensao.setChecked(false);
        }

        if(((mLeitura.Eventos >> 2) & 0x01) == 0x01)
        {
            // Bit 0 ligado indicando Sobretensão
            cbSobretensao.setChecked(true);
        }
        else
        {
            cbSobretensao.setChecked(false);
        }

        if(((mLeitura.Eventos >> 3) & 0x01) == 0x01)
        {
            // Bit 0 ligado indicando Abertura de Tampa
            cbAberturaTampa.setChecked(true);
        }
        else
        {
            cbAberturaTampa.setChecked(false);
        }

        if((mLeitura.Ciclos == 0x00) || (mLeitura.Ciclos  == 0x01))
        {
            // Ciclo Padrão
            rbCicloPadrao.setChecked(true);
        }
        else
        {
            rbCicloPadrao.setChecked(false);
        }

        if(mLeitura.Ciclos == 0x02)
        {
            // Ciclo Etapa 2
            rbCicloEtapa2.setChecked(true);
        }
        else
        {
            rbCicloEtapa2.setChecked(false);
        }

        if(mLeitura.RepeticoesEtapa1 > -1)
        {
            etRepeticoes.setText(String.valueOf(mLeitura.RepeticoesEtapa1));
        }

        if(mLeitura.Intervalo1 > -1)
        {
            etIntervalo1.setText(String.valueOf(mLeitura.Intervalo1));
        }

        if(mLeitura.Intervalo2 > -1)
        {
            etIntervalo2.setText(String.valueOf(mLeitura.Intervalo2));
        }

        if((!mLeitura.TelefoneKeepAlive.contains("00000"))&&(((byte)(mLeitura.TelefoneKeepAlive.length())) > 6))
        {
            etTelefoneKeepAlive.setText(mLeitura.TelefoneKeepAlive);
            cbTelefoneKeepAlive.setChecked(true);
            cbTelefoneKeepAlive.setEnabled(true);
        }
        else
        {
            etTelefoneKeepAlive.setText("");
            cbTelefoneKeepAlive.setChecked(false);
            cbTelefoneKeepAlive.setEnabled(false);
        }

        if(mLeitura.FrequenciaKeepAlive > -1)
        {
            etFrequenciaKeepAlive.setText(String.valueOf(mLeitura.FrequenciaKeepAlive));
        }

        if(mLeitura.ModoPrateleira == 0x00)
        {
            tbPrateleira.setChecked(true);
        }

        if(mLeitura.isLeitura)
        {
            cbTelefone1.setEnabled(true);
            cbTelefone2.setEnabled(true);
            cbTelefone3.setEnabled(true);
            cbTelefone4.setEnabled(true);
            cbTelefoneKeepAlive.setEnabled(true);
        }
    }

    private void createDialogMonitoramento() {
        dialogMonitoramento = new Dialog(this);
        dialogMonitoramento.setContentView(R.layout.dialog_monitoramento_easy_trafo);
        if (dialogMonitoramento.getWindow() != null)
            dialogMonitoramento.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        someComBotoesDoMonitoramento();

        CheckBox cbPotenciaNominal = dialogMonitoramento.findViewById(R.id.monitoramento_potencia_nominal_check);
        Spinner spPotenciaNominal = dialogMonitoramento.findViewById(R.id.monitoramento_potencia_nominal);
        CheckBox cbPapelTermoestabilizado = dialogMonitoramento.findViewById(R.id.monitoramento_papel_termoestabilizado_check);
        Switch switchPapelTermoestabilizado = dialogMonitoramento.findViewById(R.id.monitoramento_papel_termoestabilizado);
        CheckBox cbSobreTensaoA = dialogMonitoramento.findViewById(R.id.monitoramento_nivel_sobretensao_fase_A_check);
        EditText etSobreTensaoA = dialogMonitoramento.findViewById(R.id.monitoramento_nivel_sobretensao_fase_A_result);
        CheckBox cbSobreTensaoB = dialogMonitoramento.findViewById(R.id.monitoramento_nivel_sobretensao_fase_B_check);
        EditText etSobreTensaoB = dialogMonitoramento.findViewById(R.id.monitoramento_nivel_sobretensao_fase_B_result);
        CheckBox cbSobreTensaoC = dialogMonitoramento.findViewById(R.id.monitoramento_nivel_sobretensao_fase_C_check);
        EditText etSobreTensaoC = dialogMonitoramento.findViewById(R.id.monitoramento_nivel_sobretensao_fase_C_result);
        CheckBox cbSubTensaoA = dialogMonitoramento.findViewById(R.id.monitoramento_nivel_subtensao_fase_A_check);
        EditText etSubTensaoA = dialogMonitoramento.findViewById(R.id.monitoramento_nivel_subtensao_fase_A_result);
        CheckBox cbSubTensaoB = dialogMonitoramento.findViewById(R.id.monitoramento_nivel_subtensao_fase_B_check);
        EditText etSubTensaoB = dialogMonitoramento.findViewById(R.id.monitoramento_nivel_subtensao_fase_B_result);
        CheckBox cbSubTensaoC = dialogMonitoramento.findViewById(R.id.monitoramento_nivel_subtensao_fase_C_check);
        EditText etSubTensaoC = dialogMonitoramento.findViewById(R.id.monitoramento_nivel_subtensao_fase_C_result);

        dialogMonitoramento.findViewById(R.id.monitoramento_btn_close).setOnClickListener(v -> dialogMonitoramento.dismiss());

        dialogMonitoramento.findViewById(R.id.monitoramento_btn_luz).setOnClickListener(v -> {
            mEB90.setNumeroTransformador(0);
            enviaEB90();
        });

        dialogMonitoramento.findViewById(R.id.monitoramento_btn_forca_1).setOnClickListener(v -> {
            mEB90.setNumeroTransformador(1);
            enviaEB90();
        });

        dialogMonitoramento.findViewById(R.id.monitoramento_btn_forca_2).setOnClickListener(v -> {
            mEB90.setNumeroTransformador(2);
            enviaEB90();
        });

        dialogMonitoramento.findViewById(R.id.monitoramento_enviar_alteracao).setOnClickListener(v -> {
            mEB90.setLeitura(false);

            //Papel Termoestabilizado
            mEB90.setAlteracaoPapelTermoestabilizado(cbPapelTermoestabilizado.isChecked());
            mEB90.setColocarPapelTermoestabilizado(switchPapelTermoestabilizado.isChecked());

            //Potencia Nominal
            mEB90.setAlteracaoPotenciaNominal(cbPotenciaNominal.isChecked());
            mEB90.setPotenciaNominalTransformador(traduzSpinnerPotenciaNominalMonitoramento(spPotenciaNominal.getSelectedItemPosition()));

            mEB90.setAlteracaoSobretensaoA(cbSobreTensaoA.isChecked());
            if (!etSobreTensaoA.getText().toString().isEmpty()) {
                int sobreTensaoA = Integer.parseInt(etSobreTensaoA.getText().toString());
                if (testaSobreTensaoMonitoramento(sobreTensaoA)) {
                    mEB90.setSobretensaoA(sobreTensaoA * 10);
                } else {
                    Toast.makeText(DeviceActivity.this, "Nível de Sobretensão deve estar entre 100 e 300", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            mEB90.setAlteracaoSobretensaoB(cbSobreTensaoB.isChecked());
            if (!etSobreTensaoB.getText().toString().isEmpty()) {
                int sobreTensaoB = Integer.parseInt(etSobreTensaoB.getText().toString());
                if (testaSobreTensaoMonitoramento(sobreTensaoB)) {
                    mEB90.setSobretensaoB(sobreTensaoB * 10);
                } else {
                    Toast.makeText(DeviceActivity.this, "Nível de Sobretensão deve estar entre 100 e 300", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            mEB90.setAlteracaoSobretensaoC(cbSobreTensaoC.isChecked());
            if (!etSobreTensaoC.getText().toString().isEmpty()) {
                int sobreTensaoC = Integer.parseInt(etSobreTensaoC.getText().toString());
                if (testaSobreTensaoMonitoramento(sobreTensaoC)) {
                    mEB90.setSobretensaoC(sobreTensaoC * 10);
                } else {
                    Toast.makeText(DeviceActivity.this, "Nível de Sobretensão deve estar entre 100 e 300", Toast.LENGTH_SHORT).show();
                    return;
                }
            }


            mEB90.setAlteracaoSubtensaoA(cbSubTensaoA.isChecked());
            if (!etSubTensaoA.getText().toString().isEmpty()) {
                int subTensaoA = Integer.parseInt(etSubTensaoA.getText().toString());
                if (testaSubTensaoMonitoramento(subTensaoA)) {
                    mEB90.setSubtensaoA(subTensaoA * 10);
                } else {
                    Toast.makeText(DeviceActivity.this, "Nível de Subtensão deve estar entre 0 e 200", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            mEB90.setAlteracaoSubtensaoB(cbSubTensaoB.isChecked());

            if (!etSubTensaoB.getText().toString().isEmpty()) {
                int subTensaoB = Integer.parseInt(etSubTensaoB.getText().toString());
                if (testaSubTensaoMonitoramento(subTensaoB)) {
                    mEB90.setSubtensaoB(subTensaoB * 10);
                } else {
                    Toast.makeText(DeviceActivity.this, "Nível de Subtensão deve estar entre 0 e 200", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            mEB90.setAlteracaoSubtensaoC(cbSubTensaoC.isChecked());
            if (!etSubTensaoC.getText().toString().isEmpty()) {
                int subTensaoC = Integer.parseInt(etSubTensaoC.getText().toString());
                if (testaSubTensaoMonitoramento(subTensaoC)) {
                    mEB90.setSubtensaoC(subTensaoC * 10);
                } else {
                    Toast.makeText(DeviceActivity.this, "Nível de Subtensão deve estar entre 0 e 200", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            enviaEB90();

            cbPapelTermoestabilizado.setChecked(false);
            cbPotenciaNominal.setChecked(false);
            cbSobreTensaoA.setChecked(false);
            cbSobreTensaoB.setChecked(false);
            cbSobreTensaoC.setChecked(false);
            cbSubTensaoA.setChecked(false);
            cbSubTensaoB.setChecked(false);
            cbSubTensaoC.setChecked(false);
        });

        if (dadosCabecalhoQEE.tipoLigacao == 6) { //verificando se é delta aterrado
            dialogMonitoramento.findViewById(R.id.monitoramento_btn_forca_1).setVisibility(VISIBLE);
            dialogMonitoramento.findViewById(R.id.monitoramento_btn_forca_2).setVisibility(VISIBLE);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.potencia_nominal_monitoramento_menor_que_cem));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spPotenciaNominal.setAdapter(adapter);
        } else if (dadosCabecalhoQEE.tipoLigacao == 4) {
            dialogMonitoramento.findViewById(R.id.monitoramento_btn_luz).setVisibility(GONE);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.potencia_nominal_monitoramento_menor_que_cem));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spPotenciaNominal.setAdapter(adapter);
        } else {
            dialogMonitoramento.findViewById(R.id.monitoramento_btn_luz).setVisibility(GONE);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.potencia_nominal_monitoramento_maior_que_cem));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spPotenciaNominal.setAdapter(adapter);
        }

        dialogMonitoramento.setCancelable(false);
        dialogMonitoramento.create();
        dialogMonitoramento.show();
    }

    private void createDialogConfiguracaoNIC() {

        dialogConfiguracaoNIC = new Dialog(this);
        dialogConfiguracaoNIC.setContentView(R.layout.dialog_configuracaonic_easy_trafo);
        if (dialogConfiguracaoNIC.getWindow() != null)
            dialogConfiguracaoNIC.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        //someComBotoesDaConfiguracaoNIC();

        CheckBox cbTelefone1 = dialogConfiguracaoNIC.findViewById(R.id.cb_configuracaonic_telefone1_check);
        CheckBox cbTelefone2 = dialogConfiguracaoNIC.findViewById(R.id.cb_configuracaonic_telefone2_check);
        CheckBox cbTelefone3 = dialogConfiguracaoNIC.findViewById(R.id.cb_configuracaonic_telefone3_check);
        CheckBox cbTelefone4 = dialogConfiguracaoNIC.findViewById(R.id.cb_configuracaonic_telefone4_check);
        Switch tbPrateleira = dialogConfiguracaoNIC.findViewById(R.id.modo_prateleira);

        EditText titulo = dialogConfiguracaoNIC.findViewById(R.id.titulo);

        EditText etTelefone1 = dialogConfiguracaoNIC.findViewById(R.id.etTelefone1);
        EditText etTelefone2 = dialogConfiguracaoNIC.findViewById(R.id.etTelefone2);
        EditText etTelefone3 = dialogConfiguracaoNIC.findViewById(R.id.etTelefone3);
        EditText etTelefone4 = dialogConfiguracaoNIC.findViewById(R.id.etTelefone4);

        CheckBox cbAberturaTampa = dialogConfiguracaoNIC.findViewById(R.id.cb_abertura_tampa);
        CheckBox cbFaltaEnergia = dialogConfiguracaoNIC.findViewById(R.id.cb_falta_energia);
        CheckBox cbSubtensao = dialogConfiguracaoNIC.findViewById(R.id.cb_subtensao_ciclos);
        CheckBox cbSobretensao = dialogConfiguracaoNIC.findViewById(R.id.cb_sobretensao_ciclos);

        RadioButton rbCicloPadrao = dialogConfiguracaoNIC.findViewById(R.id.rb_ciclo_padrao);
        RadioButton rbCicloEtapa2 = dialogConfiguracaoNIC.findViewById(R.id.rb_etapa2);

        EditText etRepeticoes = dialogConfiguracaoNIC.findViewById(R.id.etRepeticoes);
        EditText etIntervalo1 = dialogConfiguracaoNIC.findViewById(R.id.etIntervalo1);
        EditText etIntervalo2 = dialogConfiguracaoNIC.findViewById(R.id.etIntervalo2);

        CheckBox cbTelefoneKeepAlive = dialogConfiguracaoNIC.findViewById(R.id.cb_Telefone_Keep_Alive);
        EditText etTelefoneKeepAlive = dialogConfiguracaoNIC.findViewById(R.id.etTelefoneKeepAlive);
        EditText etFrequenciaKeepAlive = dialogConfiguracaoNIC.findViewById(R.id.etFrequenciaKeepAlive);



        dialogConfiguracaoNIC.findViewById(R.id.etTelefone1).setOnClickListener(v -> {
            cbTelefone1.setEnabled(true);
        });

        dialogConfiguracaoNIC.findViewById(R.id.etTelefone2).setOnClickListener(v -> {
            cbTelefone2.setEnabled(true);
        });

        dialogConfiguracaoNIC.findViewById(R.id.etTelefone3).setOnClickListener(v -> {
            cbTelefone3.setEnabled(true);
        });

        dialogConfiguracaoNIC.findViewById(R.id.etTelefone4).setOnClickListener(v -> {
            cbTelefone4.setEnabled(true);
        });

        dialogConfiguracaoNIC.findViewById(R.id.etTelefoneKeepAlive).setOnClickListener(v -> {
            cbTelefoneKeepAlive.setEnabled(true);
        });

        dialogConfiguracaoNIC.findViewById(R.id.cb_configuracaonic_telefone1_check).setOnClickListener(v -> {
            bAlteracaoTelefoneDF = true;
            if(cbTelefone1.isChecked())
            {
                etTelefone1.setEnabled(true);
            }
            else
            {
                etTelefone1.setEnabled(false);
                etTelefone1.setText("");
            }
        });

        dialogConfiguracaoNIC.findViewById(R.id.cb_configuracaonic_telefone2_check).setOnClickListener(v -> {
            bAlteracaoTelefoneDF = true;
            if(cbTelefone2.isChecked())
            {
                etTelefone2.setEnabled(true);
            }
            else
            {
                etTelefone2.setEnabled(false);
                etTelefone2.setText("");
            }
        });

        dialogConfiguracaoNIC.findViewById(R.id.cb_configuracaonic_telefone3_check).setOnClickListener(v -> {
            bAlteracaoTelefoneDF = true;
            if(cbTelefone3.isChecked())
            {
                etTelefone3.setEnabled(true);
            }
            else
            {
                etTelefone3.setEnabled(false);
                etTelefone3.setText("");
            }
        });

        dialogConfiguracaoNIC.findViewById(R.id.cb_configuracaonic_telefone4_check).setOnClickListener(v -> {
            bAlteracaoTelefoneDF = true;
            if(cbTelefone4.isChecked())
            {
                etTelefone4.setEnabled(true);
            }
            else
            {
                etTelefone4.setEnabled(false);
                etTelefone4.setText("");
            }
        });

        dialogConfiguracaoNIC.findViewById(R.id.cb_Telefone_Keep_Alive).setOnClickListener(v -> {
            bAlteracaoTelefoneKeepAlive = true;
            if(cbTelefoneKeepAlive.isChecked())
            {
                etTelefoneKeepAlive.setEnabled(true);
            }
            else
            {
                etTelefoneKeepAlive.setEnabled(false);
                etTelefoneKeepAlive.setText("");
            }
        });

        dialogConfiguracaoNIC.findViewById(R.id.configuracaonic_btn_close).setOnClickListener(v -> dialogConfiguracaoNIC.dismiss());

        dialogConfiguracaoNIC.findViewById(R.id.configuracaonic_btn_forca_1).setOnClickListener(v -> {
            //mEB90.setNumeroTransformador(1);
            //enviaEB90();
        });

        dialogConfiguracaoNIC.findViewById(R.id.configuracaonic_btn_forca_2).setOnClickListener(v -> {
            //mEB90.setNumeroTransformador(2);
            //enviaEB90();
        });

        dialogConfiguracaoNIC.findViewById(R.id.configuracaonic_salvar_predefinição).setOnClickListener(v -> {

            String nomeXML = "";
            String Telefone1Aux = "00000000000";
            String Telefone2Aux = "00000000000";
            String Telefone3Aux = "00000000000";
            String Telefone4Aux = "00000000000";
            String TelefoneKeepAliveAux = "00000000000";
            String faltaEnergia = "0";
            String aberturaTampa = "0";
            String subtensao = "0";
            String sobretensao = "0";
            int iciclos = 1 ;
            int irepeticao = 0;
            int intervalo1 = 0;
            int intervalo2 = 0;
            int iKeepAliveFrequencia = 0;
            String etKeepAliveValidade = "0";
            String modoPrateleira = "0";

            String etRepeticoesAux = "0";
            String etIntervalo1Aux = "0";
            String etIntervalo2Aux = "0";
            String etFrequenciaKeepAliveAux = "0";


            byte EventosAux = 0;
            byte RepeticoesAux = 0;
            int Intervalo1Aux = 0;
            int Intervalo2Aux = 0;
            byte Repeticoes = 0;
            int Intervalo1 = 0;
            int Intervalo2 = 0;
            byte FrequenciaKeepAliveAux = 0;
            byte TamCampo = 0;


            if(cbTelefone1.isChecked())
            {
                Telefone1Aux = etTelefone1.getText().toString();
                TamCampo = (byte)Telefone1Aux.length();
                if(TamCampo > 0)
                {
                    if(TamCampo < 11)
                    {
                        for(byte i = 0; i < (11-TamCampo);i++)
                        {
                            Telefone1Aux = "0" + Telefone1Aux;
                        }
                    }
                    else
                    {
                        Telefone1Aux = Telefone1Aux.substring(0,11);
                    }
                }
                else
                {
                    Telefone1Aux = "00000000000";
                }
            }

            if(cbTelefone2.isChecked())
            {
                Telefone2Aux = etTelefone2.getText().toString();
                TamCampo = (byte)(Telefone2Aux.length());
                if(TamCampo > 0)
                {
                    if(TamCampo < 11)
                    {
                        for(byte i = 0; i < (11-TamCampo);i++)
                        {
                            Telefone2Aux = "0" + Telefone2Aux;
                        }
                    }
                    else
                    {
                        Telefone2Aux = Telefone2Aux.substring(0,11);
                    }
                }
                else
                {
                    Telefone2Aux = "00000000000";
                }
            }

            if(cbTelefone3.isChecked())
            {
                Telefone3Aux = etTelefone3.getText().toString();
                TamCampo = (byte)(Telefone3Aux.length());
                if(TamCampo > 0)
                {
                    if(TamCampo < 11)
                    {
                        for(byte i = 0; i < (11-TamCampo);i++)
                        {
                            Telefone3Aux = "0" + Telefone3Aux;
                        }
                    }
                    else
                    {
                        Telefone3Aux = Telefone3Aux.substring(0,11);
                    }
                }
                else
                {
                    Telefone3Aux = "00000000000";
                }
            }

            if(cbTelefone4.isChecked())
            {
                Telefone4Aux = etTelefone4.getText().toString();
                TamCampo = (byte)(Telefone4Aux.length());
                if(TamCampo > 0)
                {
                    if(TamCampo < 11)
                    {
                        for(byte i = 0; i < (11-TamCampo);i++)
                        {
                            Telefone4Aux = "0" + Telefone4Aux;
                        }
                    }
                    else
                    {
                        Telefone4Aux = Telefone4Aux.substring(0,11);
                    }
                }
                else
                {
                    Telefone4Aux = "00000000000";
                }
            }

            if(cbFaltaEnergia.isChecked())
            {
                faltaEnergia = "1";
            }
            else
            {
                faltaEnergia = "0";
            }

            if(cbAberturaTampa.isChecked())
            {
                aberturaTampa = "1";
            }
            else
            {
                aberturaTampa= "0";
            }

            if(cbSubtensao.isChecked())
            {
                subtensao = "1";
            }
            else
            {
                subtensao = "0";
            }

            if(cbSobretensao.isChecked())
            {
                sobretensao = "1";
            }
            else
            {
                sobretensao = "0";
            }

            if(rbCicloPadrao.isChecked())
            {
                iciclos = 1;
            }
            else if(rbCicloEtapa2.isChecked())
            {
                iciclos = 2;
            }
            else
            {
                iciclos = 0;
            }

            //if(rbCicloPadrao.isChecked() && (iCiclos == 0x02))
            //{
            //    iciclos = 1;
            //}
            //else if(rbCicloEtapa2.isChecked() && ((iCiclos == 0x00) || (iCiclos == 0x01)))
            //{
            //    iciclos = 2;
            //}
            //else
            //{
            //    iciclos = 0;
            //}

            etRepeticoesAux = etRepeticoes.getText().toString();
            if(!etRepeticoesAux.isEmpty())
            {
                try {
                    irepeticao = Byte.parseByte(etRepeticoesAux);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Número de Repetições inválido", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                // Qtd de Repetições nao foi digitado
                irepeticao = 0;
            }

            etIntervalo1Aux = etIntervalo1.getText().toString();
            if(!etIntervalo1Aux.isEmpty())
            {
                try {
                    int d = Integer.parseInt(etIntervalo1Aux);

                    if(iIntervalo1 != d)
                    {
                        intervalo1 = d;
                    }
                    else
                    {
                        intervalo1 = iIntervalo1;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Valor do Intervalo1 inválido", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                intervalo1 = 0;
            }

            etIntervalo2Aux = etIntervalo2.getText().toString();
            if(!etIntervalo2Aux.isEmpty())
            {
                try {
                    int d = Integer.parseInt(etIntervalo2Aux);

                    if(iIntervalo2 != d)
                    {
                        intervalo2 = d;
                    }
                    else
                    {
                        intervalo2 = iIntervalo2;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Valor do Intervalo2 inválido", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                intervalo2 = 0;
            }
            if(cbTelefoneKeepAlive.isChecked())
            {
                TelefoneKeepAliveAux = etTelefoneKeepAlive.getText().toString();
                TamCampo = (byte)(TelefoneKeepAliveAux.length());
                if(TamCampo > 0)
                {
                    if(TamCampo < 11)
                    {
                        for (byte i = 0; i < (11 - TamCampo); i++)
                        {
                            TelefoneKeepAliveAux = "0" + TelefoneKeepAliveAux;
                        }
                    }
                    else
                    {
                        TelefoneKeepAliveAux = TelefoneKeepAliveAux.substring(0,11);
                    }
                }
                else
                {
                    TelefoneKeepAliveAux = "00000000000";
                }

                etKeepAliveValidade = "1";
            }
            else
            {
                etKeepAliveValidade = "0";
            }
            etFrequenciaKeepAliveAux = etFrequenciaKeepAlive.getText().toString();
            if(!etFrequenciaKeepAliveAux.isEmpty())
            {
                try {
                    byte d = Byte.parseByte(etFrequenciaKeepAliveAux);

                    if(iFrequenciaKeepAlive != d)
                    {
                        iKeepAliveFrequencia = d;
                    }
                    else{
                        iKeepAliveFrequencia = iFrequenciaKeepAlive;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Valor da Frequência Keep Alive inválida", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                iKeepAliveFrequencia = 0;
            }

            if(tbPrateleira.isChecked()){
                modoPrateleira = "1";
            }
            else{
                modoPrateleira = "0";
            }

            String d = titulo.getText().toString();
            d = d.replace(' ', '_');

            TamCampo = (byte)(d.length());

            if(TamCampo > 0){

                nomeXML = d;

                if(Arquivo.createDirNIC()){
                    File localNIC = new File( Arquivo.pathNIC() + "/" + nomeXML+ ".xml");

                    FileOutputStream os;
                    try {
                        if (localNIC.createNewFile()) {
                            os = new FileOutputStream(localNIC);
                            try {
                            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                            Document dom = builder.newDocument();

                            Element root = dom.createElement("NIC");
                            dom.appendChild(root);

                                Element falhas = dom.createElement("Deteccao_falhas");
                                Attr n1 = dom.createAttribute("telefone1");
                                n1.setValue(Telefone1Aux);
                                falhas.setAttributeNode(n1);
                                Attr n2 = dom.createAttribute("telefone2");
                                n2.setValue(Telefone2Aux);
                                falhas.setAttributeNode(n2);
                                Attr n3 = dom.createAttribute("telefone3");
                                n3.setValue(Telefone3Aux);
                                falhas.setAttributeNode(n3);
                                Attr n4 = dom.createAttribute("telefone4");
                                n4.setValue(Telefone4Aux);
                                falhas.setAttributeNode(n4);


                                Element eventos = dom.createElement("Eventos");
                                Attr energia = dom.createAttribute("falta_energia");
                                energia.setValue(faltaEnergia);
                                eventos.setAttributeNode(energia);
                                Attr tampa = dom.createAttribute("abertura_tampa");
                                tampa.setValue(aberturaTampa);
                                eventos.setAttributeNode(tampa);
                                Attr sub = dom.createAttribute("subtensao");
                                sub.setValue(subtensao);
                                eventos.setAttributeNode(sub);
                                Attr sobre = dom.createAttribute("sobretensao");
                                sobre.setValue(sobretensao);
                                eventos.setAttributeNode(sobre);


                                Element Ciclos = dom.createElement("Ciclos");
                                Attr etapa = dom.createAttribute("etapa");
                                etapa.setValue(String.valueOf(iciclos));
                                Ciclos.setAttributeNode(etapa);
                                Attr repeticoes = dom.createAttribute("repeticoes");
                                repeticoes.setValue(String.valueOf(irepeticao));
                                Ciclos.setAttributeNode(repeticoes);
                                Attr i1 = dom.createAttribute("intervalo1");
                                i1.setValue(String.valueOf(intervalo1));
                                Ciclos.setAttributeNode(i1);
                                Attr i2 = dom.createAttribute("intervalo2");
                                i2.setValue(String.valueOf(intervalo2));
                                Ciclos.setAttributeNode(i2);


                                Element keep = dom.createElement("Keep_alive");
                                Attr kt = dom.createAttribute("telefone");
                                kt.setValue(TelefoneKeepAliveAux);
                                keep.setAttributeNode(kt);
                                Attr val = dom.createAttribute("validade");
                                val.setValue(String.valueOf(etKeepAliveValidade));
                                keep.setAttributeNode(val);
                                Attr freq = dom.createAttribute("frequencia");
                                freq.setValue(String.valueOf(iKeepAliveFrequencia));
                                keep.setAttributeNode(freq);

                                Element prat = dom.createElement("Modo_prateleira");
                                Attr modo = dom.createAttribute("comando");
                                modo.setValue(modoPrateleira);
                                prat.setAttributeNode(modo);

                                root.appendChild(falhas);
                                root.appendChild(eventos);
                                root.appendChild(Ciclos);
                                root.appendChild(keep);
                                root.appendChild(prat);

                                Transformer tr = TransformerFactory.newInstance().newTransformer();
                                tr.transform(new DOMSource(dom), new StreamResult(localNIC));
                                Toast.makeText(this, "Arquivo Salvo com sucesso", Toast.LENGTH_SHORT).show();
                                titulo.setText("");
                            }
                            catch (Exception ex){

                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                /*
                AlertDialog dialog = new AlertDialog.Builder(this,
                        android.R.style.Theme_Material_Dialog_Alert)
                        .setTitle("Aviso")
                        .setMessage("Aguardando confirmação de alteração da configuração.")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setCancelable(false).setCancelable(false)
                        .setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    private static final int AUTO_DISMISS_MILLIS = 180000;
                    @Override
                    public void onShow(final DialogInterface dialog) {
                        final Button defaultButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                        defaultButton.setClickable(false);
                        final CharSequence negativeButtonText = "";//defaultButton.getText();
                        new CountDownTimer(AUTO_DISMISS_MILLIS, 100) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                if(bAlterandoConfigRemota == false){
                                    iContadorLeituraEB17Enviado = 0;
                                    bAlterandoConfigRemota = false;
                                    dialog.cancel();
                                }
                                defaultButton.setText(String.format(Locale.getDefault(), "%s %d",negativeButtonText,
                                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1 //add one so it never displays zero
                                ));
                            }
                            @Override
                            public void onFinish() {
                                if (((AlertDialog) dialog).isShowing()) {
                                    dialog.cancel();
                                    if(bAlterandoConfigRemota){
                                        iContadorLeituraEB17Enviado = 0;
                                        bAlterandoConfigRemota = false;
                                        Toast.makeText(((AlertDialog) dialog).getContext(), "Erro ao alterar cfg da remota.", Toast.LENGTH_SHORT).show();
                                        //Toast.makeText(((AlertDialog) dialog).getContext(), "Sem resposta da Remota.", Toast.LENGTH_SHORT).show();
                                        dialog.cancel();
                                    }
                                }
                            }
                        }.start();
                    }
                });
                dialog.show();

                Toast.makeText(this, "Enviando alteração de configuração de NIC", Toast.LENGTH_SHORT).show();
                */
                etTelefone1.setEnabled(true);
                etTelefone2.setEnabled(true);
                etTelefone3.setEnabled(true);
                etTelefone4.setEnabled(true);
                etTelefoneKeepAlive.setEnabled(true);

                cbTelefone1.setEnabled(true);
                cbTelefone2.setEnabled(true);
                cbTelefone3.setEnabled(true);
                cbTelefone4.setEnabled(true);
                cbTelefoneKeepAlive.setEnabled(true);
            }
            else{
                Toast.makeText(this, "Nome do arquivo não especificado", Toast.LENGTH_SHORT).show();

            }
        });

        dialogConfiguracaoNIC.findViewById(R.id.configuracaonic_enviar_alteracao).setOnClickListener(v -> {
            String Telefone1Aux = "00000000000";
            String Telefone2Aux = "00000000000";
            String Telefone3Aux = "00000000000";
            String Telefone4Aux = "00000000000";
            String TelefonesDFAux = Telefone1Aux + Telefone2Aux + Telefone3Aux + Telefone4Aux;
            String TelefoneKeepAliveAux = "00000000000";

            String etRepeticoesAux = "0";
            String etIntervalo1Aux = "0";
            String etIntervalo2Aux = "0";
            String etFrequenciaKeepAliveAux = "0";

            boolean bAlteracaoEventos = false;

            byte QtdTelefonesAux= 0;
            byte EventosAux = 0;
            byte RepeticoesAux = 0;
            int Intervalo1Aux = 0;
            int Intervalo2Aux = 0;
            byte Repeticoes = 0;
            int Intervalo1 = 0;
            int Intervalo2 = 0;
            byte FrequenciaKeepAliveAux = 0;
            byte TamCampo = 0;

            mEB17 = new ComandoAbsoluto.EB17().comNumerMedidor(monitoramentoNumeroEasy);
            mEB17.setLeitura(false);

            if(cbTelefone1.isChecked())
            {
                Telefone1Aux = etTelefone1.getText().toString();
                TamCampo = (byte)Telefone1Aux.length();
                if(TamCampo > 0)
                {
                    if(TamCampo < 11)
                    {
                        for(byte i = 0; i < (11-TamCampo);i++)
                        {
                            Telefone1Aux = "0" + Telefone1Aux;
                        }
                    }
                    else
                    {
                        Telefone1Aux = Telefone1Aux.substring(0,11);
                    }
                }
                else
                {
                    Telefone1Aux = "00000000000";
                }
                bAlteracaoTelefoneDF = true;
                QtdTelefonesAux++;
            }

            if(cbTelefone2.isChecked())
            {
                Telefone2Aux = etTelefone2.getText().toString();
                TamCampo = (byte)(Telefone2Aux.length());
                if(TamCampo > 0)
                {
                    if(TamCampo < 11)
                    {
                        for(byte i = 0; i < (11-TamCampo);i++)
                        {
                            Telefone2Aux = "0" + Telefone2Aux;
                        }
                    }
                    else
                    {
                        Telefone2Aux = Telefone2Aux.substring(0,11);
                    }
                }
                else
                {
                    Telefone2Aux = "00000000000";
                }
                bAlteracaoTelefoneDF = true;
                QtdTelefonesAux++;
            }

            if(cbTelefone3.isChecked())
            {
                Telefone3Aux = etTelefone3.getText().toString();
                TamCampo = (byte)(Telefone3Aux.length());
                if(TamCampo > 0)
                {
                    if(TamCampo < 11)
                    {
                        for(byte i = 0; i < (11-TamCampo);i++)
                        {
                            Telefone3Aux = "0" + Telefone3Aux;
                        }
                    }
                    else
                    {
                        Telefone3Aux = Telefone3Aux.substring(0,11);
                    }
                }
                else
                {
                    Telefone3Aux = "00000000000";
                }
                bAlteracaoTelefoneDF = true;
                QtdTelefonesAux++;
            }

            if(cbTelefone4.isChecked())
            {
                Telefone4Aux = etTelefone4.getText().toString();
                TamCampo = (byte)(Telefone4Aux.length());
                if(TamCampo > 0)
                {
                    if(TamCampo < 11)
                    {
                        for(byte i = 0; i < (11-TamCampo);i++)
                        {
                            Telefone4Aux = "0" + Telefone4Aux;
                        }
                    }
                    else
                    {
                        Telefone4Aux = Telefone4Aux.substring(0,11);
                    }
                }
                else
                {
                    Telefone4Aux = "00000000000";
                }
                bAlteracaoTelefoneDF = true;
                QtdTelefonesAux++;
            }

            if(!(sTelefone1.contentEquals(Telefone1Aux)) || !(sTelefone2.contentEquals(Telefone2Aux)) || !(sTelefone3.contentEquals(Telefone3Aux)) || !(sTelefone4.contentEquals(Telefone4Aux)))
            {
                // Algum dos telefones marcados
                bAlteracaoTelefoneDF = true;
            }
            else
            {
                // Nenhum telefone marcado
                bAlteracaoTelefoneDF = false;
            }

            if(bAlteracaoTelefoneDF)
            {
                mEB17.setAlteracaoTelefones(true);
                TelefonesDFAux = Telefone1Aux + Telefone2Aux + Telefone3Aux + Telefone4Aux;
                mEB17.setTelefonesDeteccaoFalhas(TelefonesDFAux);
                mEB17.setQtdTelefones(QtdTelefonesAux);
            }else
            {
                mEB17.setAlteracaoTelefones(false);
            }

            if(cbFaltaEnergia.isChecked())
            {
                bAlteracaoEventos = true;
                EventosAux |= 0x01;
            }
            else
            {
                EventosAux &= 0xFE;
            }

            if(cbSubtensao.isChecked())
            {
                bAlteracaoEventos = true;
                EventosAux |= 0x02;
            }
            else
            {
                EventosAux &= 0xFD;
            }

            if(cbSobretensao.isChecked())
            {
                bAlteracaoEventos = true;
                EventosAux |= 0x04;
            }
            else
            {
                EventosAux &= 0xFB;
            }

            if(cbAberturaTampa.isChecked())
            {
                bAlteracaoEventos = true;
                EventosAux |= 0x08;
            }
            else
            {
                EventosAux &= 0xF7;
            }

            if(iEventos != EventosAux)
            {
                // Houve mudança nos eventos
                bAlteracaoEventos = true;
            }
            else
            {
                bAlteracaoEventos = false;
            }

            if(bAlteracaoEventos)
            {
                mEB17.setAlteracaoEventos(true);
                mEB17.setEventos(EventosAux);
            }
            else
            {
                mEB17.setAlteracaoEventos(false);
            }

            if(rbCicloPadrao.isChecked() && (iCiclos == 0x02))
            {
                mEB17.setAlteracaoCiclos(true);
                mEB17.setCiclos((byte)0x00);
            }
            else if(rbCicloEtapa2.isChecked() && ((iCiclos == 0x00) || (iCiclos == 0x01)))
            {
                mEB17.setAlteracaoCiclos(true);
                mEB17.setCiclos((byte)0x02);
            }
            else
            {
                mEB17.setAlteracaoCiclos(false);
            }

            etRepeticoesAux = etRepeticoes.getText().toString();
            if(!etRepeticoesAux.isEmpty())
            {
                try {
                    byte d = Byte.parseByte(etRepeticoesAux);

                    if(iRepeticoes != d)
                    {
                        mEB17.setAlteracaoRepeticoes(true);
                        mEB17.setRepeticoesEtapa1(d);
                    }
                    else
                    {
                        mEB17.setAlteracaoRepeticoes(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Número de Repetições inválido", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                // Qtd de Repetições nao foi digitado
                mEB17.setAlteracaoRepeticoes(false);
                mEB17.setRepeticoesEtapa1((byte)0x00);
            }

            etIntervalo1Aux = etIntervalo1.getText().toString();
            if(!etIntervalo1Aux.isEmpty())
            {
                try {
                    int d = Integer.parseInt(etIntervalo1Aux);

                    if(iIntervalo1 != d)
                    {
                        mEB17.setAlteracaoIntervalo1(true);
                        mEB17.setIntervalo1(d);
                    }
                    else
                    {
                        mEB17.setAlteracaoIntervalo1(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Valor do Intervalo1 inválido", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                // Intervalo1 nao foi digitado
                mEB17.setAlteracaoIntervalo1(false);
                mEB17.setIntervalo1((int)0x00);
            }

            etIntervalo2Aux = etIntervalo2.getText().toString();
            if(!etIntervalo2Aux.isEmpty())
            {
                try {
                    int d = Integer.parseInt(etIntervalo2Aux);

                    if(iIntervalo2 != d)
                    {
                        mEB17.setAlteracaoIntervalo2(true);
                        mEB17.setIntervalo2(d);
                    }
                    else
                    {
                        mEB17.setAlteracaoIntervalo2(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Valor do Intervalo2 inválido", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                // Intervalo2 nao foi digitado
                mEB17.setAlteracaoIntervalo2(false);
                mEB17.setIntervalo2((int)0x00);
            }

            if(cbTelefoneKeepAlive.isChecked())
            {
                TelefoneKeepAliveAux = etTelefoneKeepAlive.getText().toString();
                TamCampo = (byte)(TelefoneKeepAliveAux.length());
                if(TamCampo > 0)
                {
                    if(TamCampo < 11)
                    {
                        for (byte i = 0; i < (11 - TamCampo); i++)
                        {
                            TelefoneKeepAliveAux = "0" + TelefoneKeepAliveAux;
                        }
                    }
                    else
                    {
                        TelefoneKeepAliveAux = TelefoneKeepAliveAux.substring(0,11);
                    }
                }
                else
                {
                    TelefoneKeepAliveAux = "00000000000";
                }

                //bAlteracaoTelefoneKeepAlive = true;
                //mEB17.setTelefoneKeepAlive(TelefoneKeepAliveAux);
                //mEB17.setAlteracaoTelefoneKeepAlive(true);
                mEB17.setValidade((byte)0x01);
            }
            else
            {
                //bAlteracaoTelefoneKeepAlive = true;
                //mEB17.setAlteracaoTelefoneKeepAlive(true);
                mEB17.setValidade((byte)0x00);
            }

            if(!sTelefoneKeepAlive.contentEquals(TelefoneKeepAliveAux))
            {
                // Houve alteração do telefone de keep alive
                bAlteracaoTelefoneKeepAlive = true;
                mEB17.setTelefoneKeepAlive(TelefoneKeepAliveAux);
                mEB17.setAlteracaoTelefoneKeepAlive(true);
            }
            else
            {
                // Não houve alteração de telefone de keep alive
                mEB17.setAlteracaoTelefoneKeepAlive(false);
            }

            etFrequenciaKeepAliveAux = etFrequenciaKeepAlive.getText().toString();
            if(!etFrequenciaKeepAliveAux.isEmpty())
            {
                try {
                    byte d = Byte.parseByte(etFrequenciaKeepAliveAux);

                    if(iFrequenciaKeepAlive != d)
                    {
                        mEB17.setAlteracaoFrequenciaKeepAlive(true);
                        mEB17.setFrequenciaKeepAlive(d);
                    }
                    else
                    {
                        mEB17.setAlteracaoFrequenciaKeepAlive(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Valor da Frequência Keep Alive inválida", Toast.LENGTH_SHORT).show();
                }
            }

            if(tbPrateleira.isChecked()){
                mEB17.setModoPrateleira((byte)0x00);
                if(bUltimoEstadoModoPrateleira == false){
                    mEB17.setAlteracaoModoPrateleira(true);
                }
                else
                {
                    mEB17.setAlteracaoModoPrateleira(false);
                }
            }
            else {
                mEB17.setModoPrateleira((byte)0x01);
                if(bUltimoEstadoModoPrateleira == true){
                    mEB17.setAlteracaoModoPrateleira(true);
                }
                else
                {
                    mEB17.setAlteracaoModoPrateleira(false);
                }
            }

            enviaEB17();
            bAlterandoConfigRemota = true;
            iContadorLeituraEB17Enviado = 0;

            AlertDialog dialog = new AlertDialog.Builder(this,
                    android.R.style.Theme_Material_Dialog_Alert)
                    .setTitle("Aviso")
                    .setMessage("Aguardando confirmação de alteração da configuração.")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false).setCancelable(false)
                    .setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .create();
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                private static final int AUTO_DISMISS_MILLIS = 180000;
                @Override
                public void onShow(final DialogInterface dialog) {
                    final Button defaultButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                    defaultButton.setClickable(false);
                    final CharSequence negativeButtonText = "";//defaultButton.getText();
                    new CountDownTimer(AUTO_DISMISS_MILLIS, 100) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if(bAlterandoConfigRemota == false){
                                iContadorLeituraEB17Enviado = 0;
                                bAlterandoConfigRemota = false;
                                dialog.cancel();
                            }
                            defaultButton.setText(String.format(Locale.getDefault(), "%s %d",negativeButtonText,
                                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1 //add one so it never displays zero
                            ));
                        }
                        @Override
                        public void onFinish() {

                            if (((AlertDialog) dialog).isShowing()) {
                                dialog.cancel();
                                if(bAlterandoConfigRemota){
                                    createDialogError("Erro ao alterar cfg da remota.");
                                    iContadorLeituraEB17Enviado = 0;
                                    bAlterandoConfigRemota = false;
                                    //Toast.makeText(((AlertDialog) dialog).getContext(), "Erro ao alterar cfg da remota.", Toast.LENGTH_SHORT).show();


                                    //Toast.makeText(((AlertDialog) dialog).getContext(), "Sem resposta da Remota.", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                    }.start();
                }
            });
            dialog.show();

            Toast.makeText(this, "Enviando alteração de configuração de NIC", Toast.LENGTH_SHORT).show();

            etTelefone1.setEnabled(true);
            etTelefone2.setEnabled(true);
            etTelefone3.setEnabled(true);
            etTelefone4.setEnabled(true);
            etTelefoneKeepAlive.setEnabled(true);

            cbTelefone1.setEnabled(true);
            cbTelefone2.setEnabled(true);
            cbTelefone3.setEnabled(true);
            cbTelefone4.setEnabled(true);
            cbTelefoneKeepAlive.setEnabled(true);
        });

        dialogConfiguracaoNIC.setCancelable(false);
        dialogConfiguracaoNIC.create();
        dialogConfiguracaoNIC.show();

        AlertDialog dialo = new AlertDialog.Builder(this,
                android.R.style.Theme_Material_Dialog_Alert)
                .setTitle("Aviso")
                .setMessage("Aguardando confirmação de alteração pendente.")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false).setCancelable(false)
                .setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialo, int which) {
                        dialo.cancel();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .create();
        dialo.setOnShowListener(new DialogInterface.OnShowListener() {
            private static final int AUTO_DISMISS_MILLIS = 180000;
            @Override
            public void onShow(final DialogInterface dialo) {
                final Button defaultButton = ((AlertDialog) dialo).getButton(AlertDialog.BUTTON_NEGATIVE);
                defaultButton.setClickable(false);
                final CharSequence negativeButtonText = "";//defaultButton.getText();
                new CountDownTimer(AUTO_DISMISS_MILLIS, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if(bAlterandoConfigRemota == false){
                            iContadorLeituraEB17Enviado = 0;
                            bAlterandoConfigRemota = false;
                            dialo.cancel();
                        }
                        defaultButton.setText(String.format(Locale.getDefault(), "%s %d",negativeButtonText,
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1 //add one so it never displays zero
                        ));
                    }
                    @Override
                    public void onFinish() {
                        if (((AlertDialog) dialo).isShowing()) {
                            dialo.cancel();
                            if(bAlterandoConfigRemota){
                                iContadorLeituraEB17Enviado = 0;
                                bAlterandoConfigRemota = false;
                                createDialogError("Erro ao alterar cfg da remota.");
                                //Toast.makeText(((AlertDialog) dialo).getContext(), "Erro ao alterar cfg da remota.", Toast.LENGTH_SHORT).show();
                                //Toast.makeText(((AlertDialog) dialog).getContext(), "Sem resposta da Remota.", Toast.LENGTH_SHORT).show();
                                dialo.cancel();
                            }
                        }
                    }
                }.start();
            }
        });
        dialo.show();
    }

    private boolean testaSobreTensaoMonitoramento(int sobretensao) {
        return sobretensao >= 100 && sobretensao <= 300;
    }

    private boolean testaSubTensaoMonitoramento(int subtensao) {
        return subtensao >= 0 && subtensao <= 240;
    }

    private void someComBotoesDoMonitoramento() {
        if (tipo_medidor == TipoMedidor.EASY_VOLT) {
            dialogMonitoramento.findViewById(R.id.monitoramento_potencia_nominal_check).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_potencia_nominal).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_papel_termoestabilizado_check).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_papel_termoestabilizado).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_temperatura_nivel_1_check).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_temperatura_nivel_1_result).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_temperatura_nivel_2_check).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_temperatura_nivel_2_result).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_expoente_do_oleo_check).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_expoente_do_oleo_result).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_expoente_do_enrolamento_check).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_expoente_do_enrolamento_result).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_constante_tempo_oleo_check).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_constante_tempo_oleo_result).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_constante_tempo_enrolamento_check).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_constante_tempo_enrolamento_result).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_k11_check).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_k11_result).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_k21_check).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_k21_result).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_k22_check).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_k22_result).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_constante_discretizacao_check).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_constante_discretizacao_result).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_relacao_perdas_corrente_nominal_vazio_check).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_relacao_perdas_corrente_nominal_vazio_result).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_temperatura_ponto_mais_quente_nominal_check).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_temperatura_ponto_mais_quente_nominal_result).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_elevacao_temperatura_topo_oleo_check).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_elevacao_temperatura_topo_oleo_result).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_gradiente_diferenca_temperaturas_ponto_mais_quente_topo_oleo_check).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_gradiente_diferenca_temperaturas_ponto_mais_quente_topo_oleo_result).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_nivel_1_alarme_sobretemperatura_do_mais_quente_do_transformador_check).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_nivel_1_alarme_sobretemperatura_do_mais_quente_do_transformador_result).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_nivel_2_alarme_sobretemperatura_do_mais_quente_do_transformador_check).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_nivel_2_alarme_sobretemperatura_do_mais_quente_do_transformador_result).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_nivel_sobrecorrente_fase_A_result).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_nivel_sobrecorrente_fase_A_check).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_nivel_sobrecorrente_fase_B_check).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_nivel_sobrecorrente_fase_B_result).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_nivel_sobrecorrente_fase_C_check).setVisibility(GONE);
            dialogMonitoramento.findViewById(R.id.monitoramento_nivel_sobrecorrente_fase_C_result).setVisibility(GONE);
        }
        /*
        dialogMonitoramento.findViewById(R.id.monitoramento_nivel_sobretensao_fase_A_result);
        dialogMonitoramento.findViewById(R.id.monitoramento_nivel_sobretensao_fase_B_result);
        dialogMonitoramento.findViewById(R.id.monitoramento_nivel_sobretensao_fase_C_result);
        dialogMonitoramento.findViewById(R.id.monitoramento_nivel_subtensao_fase_A_result);
        dialogMonitoramento.findViewById(R.id.monitoramento_nivel_subtensao_fase_B_result);
        dialogMonitoramento.findViewById(R.id.monitoramento_nivel_subtensao_fase_C_result);
        */
    }

    private void someComBotoesDaConfiguracaoNIC() {
        if (tipo_medidor == TipoMedidor.EASY_VOLT) {
            dialogConfiguracaoNIC.findViewById(R.id.cb_configuracaonic_telefone1_check).setVisibility(GONE);
            dialogConfiguracaoNIC.findViewById(R.id.etTelefone1).setVisibility(GONE);
            dialogConfiguracaoNIC.findViewById(R.id.cb_configuracaonic_telefone2_check).setVisibility(GONE);
            dialogConfiguracaoNIC.findViewById(R.id.etTelefone2).setVisibility(GONE);
            dialogConfiguracaoNIC.findViewById(R.id.cb_configuracaonic_telefone3_check).setVisibility(GONE);
            dialogConfiguracaoNIC.findViewById(R.id.etTelefone3).setVisibility(GONE);
            dialogConfiguracaoNIC.findViewById(R.id.cb_configuracaonic_telefone4_check).setVisibility(GONE);
            dialogConfiguracaoNIC.findViewById(R.id.etTelefone4).setVisibility(GONE);
            dialogConfiguracaoNIC.findViewById(R.id.rg_config_ciclos).setVisibility(GONE);
            dialogConfiguracaoNIC.findViewById(R.id.rg_etapa_ciclos).setVisibility(GONE);
            dialogConfiguracaoNIC.findViewById(R.id.etRepeticoes).setVisibility(GONE);
            dialogConfiguracaoNIC.findViewById(R.id.etIntervalo1).setVisibility(GONE);
            dialogConfiguracaoNIC.findViewById(R.id.etIntervalo2).setVisibility(GONE);
        }
        /*
        dialogConfiguracaoNIC.findViewById(R.id.configuracaonic_nivel_sobretensao_fase_A_result);
        dialogConfiguracaoNIC.findViewById(R.id.configuracaonic_nivel_sobretensao_fase_B_result);
        dialogConfiguracaoNIC.findViewById(R.id.configuracaonic_nivel_sobretensao_fase_C_result);
        dialogConfiguracaoNIC.findViewById(R.id.configuracaonic_nivel_subtensao_fase_A_result);
        dialogConfiguracaoNIC.findViewById(R.id.configuracaonic_nivel_subtensao_fase_B_result);
        dialogConfiguracaoNIC.findViewById(R.id.configuracaonic_nivel_subtensao_fase_C_result);
        */
    }

    private void processarRespostaEB11ParaMonitoramento(RespostaAbsoluto respostaAbsoluto) {
        mTimeoutHandler.removeCallbacksAndMessages(null);
        mHandler.removeCallbacksAndMessages(null);
        progressDialog.dismiss();

        dadosCabecalhoQEE = respostaAbsoluto.interpretaRespostaAB11Cabecalho();
        mEB90 = new ComandoAbsoluto.EB90().comNumerMedidor(monitoramentoNumeroEasy);
        if (dadosCabecalhoQEE.tipoLigacao == 6 || dadosCabecalhoQEE.tipoLigacao == 1) {
            createDialogMonitoramento();
            enviaEB90();
        } else {
            Toast.makeText(mBluetoothLeService, "Monitoramento não é válido para este tipo de ligação.\nVerifique a parametrização QEE", Toast.LENGTH_LONG).show();
        }
    }

    private int traduzSpinnerPotenciaNominalMonitoramento(int selected) {
        int i = 0;
        switch (selected) {
            case 0:
                if (dadosCabecalhoQEE.tipoLigacao == 6 || dadosCabecalhoQEE.tipoLigacao == 4) {
                    i = 250;
                } else {
                    i = 1125;
                }
                break;
            case 1:
                if (dadosCabecalhoQEE.tipoLigacao == 6 || dadosCabecalhoQEE.tipoLigacao == 4) {
                    i = 375;
                } else {
                    i = 1500;
                }
                break;
            case 2:
                if (dadosCabecalhoQEE.tipoLigacao == 6 || dadosCabecalhoQEE.tipoLigacao == 4) {
                    i = 500;
                } else {
                    i = 2250;
                }
                break;
            case 3:
                i = 750;
                break;
            case 4:
                i = 1000;
                break;
        }
        return i;
    }

    private void processarRespostaEB11(RespostaAbsoluto respostaAbsoluto) {
        Log.d(TAG, "## Resposta EB11 ...");
        byte pacote = 1;
        if (funcaoEmExecucao == TipoOperacao.InicioMemoriaMassaQEE) {
            funcaoEmExecucao = TipoOperacao.MemoriaMassaQEE;
            dadosCabecalhoQEE = respostaAbsoluto.interpretaRespostaAB11Cabecalho();
            mRespostaQEE = new byte[dadosCabecalhoQEE.numeroBytes];
            progressDialog = AlertBuilder.createProgressDialog(this, "Enviando pacote " + dadosCabecalhoQEE.pacoteAtual + " de " + dadosCabecalhoQEE.numeroPacotes);
            progressDialog.show();
            showTimeOutMM();
            enviaComandoEB11(pacote, MetodoNumeroSerial);
        } else {
            int tamanho = (respostaAbsoluto.mData.length - 11);
            if ((tamanho + dadosCabecalhoQEE.posicaoAtual) > mRespostaQEE.length) {
                tamanho = mRespostaQEE.length - dadosCabecalhoQEE.posicaoAtual;
            }
            System.arraycopy(respostaAbsoluto.getData(), 9, mRespostaQEE, dadosCabecalhoQEE.posicaoAtual, tamanho);
            dadosCabecalhoQEE.posicaoAtual += tamanho;
            //mRespostaComposta.add(respostaAbsoluto.getData());
            if (!respostaAbsoluto.interpretaRespostaEB11()) {
                dadosCabecalhoQEE.pacoteAtual++;
                progressDialog.setMessage("Enviando pacote " + dadosCabecalhoQEE.pacoteAtual + " de " + dadosCabecalhoQEE.numeroPacotes);
                showTimeOutMM();
                enviaComandoEB11(pacote, MetodoNumeroSerial);
            } else {
                mMMRunnable = null;
                mHandler.removeCallbacksAndMessages(null);
                progressDialog.setMessage("Aguarde, Processando QEE.");
                if((dadosCabecalhoQEE.grandezas & 0xFF) == 0xD7){
                    // Tem fator de potencia stSM1
                    processaQEE = respostaAbsoluto.preparaQEE();
                    //long numero_registros = dadosCabecalhoQEE.numeroRegistros;
                    for (int j = 0; j < mRespostaQEE.length; j += 31) {
                        processaQEE.preencheDados(Arrays.copyOfRange(mRespostaQEE, j, j + 31));
                    }
                }
                else if((dadosCabecalhoQEE.grandezas & 0xFF) == 0xD5){
                    // EasyVolt - Sem fator de potencia
                    processaQEESM2 = respostaAbsoluto.preparaQEESM2();
                    //long numero_registros = dadosCabecalhoQEE.numeroRegistros;
                    for (int j = 0; j < mRespostaQEE.length; j += 23) {
                        processaQEESM2.preencheDados(Arrays.copyOfRange(mRespostaQEE, j, j + 23));
                    }
                }
                Log.i(TAG, "Data de Inicio: " + dadosCabecalhoQEE.textdataInicio + " | " + dadosCabecalhoQEE.textdataFim);

                Locale br = Locale.forLanguageTag("BR");

                StringBuilder dadosCSV = new StringBuilder();
                StringBuilder dadosCSVCompleto = new StringBuilder();
                dadosCSV.append("StarMeasure\n");
                dadosCSV.append("Ponto;" + mDeviceName.substring(4) + "\n");
                dadosCSV.append("Area;" + "\n");
                dadosCSV.append("Numero de Registros;" + dadosCabecalhoQEE.numeroRegistros + "\n");
                dadosCSV.append("Intervalo da memoria QEE;10\n");
                dadosCSV.append("Data/Hora de Início;" + dadosCabecalhoQEE.textdataInicio + "\n");
                dadosCSV.append("Data/Hora de Final;" + dadosCabecalhoQEE.textdataFim + "\n");
                dadosCSV.append("TP Original;1\n");
                dadosCSV.append("TP;1\n");

                String[] items = new String[]{"Indefinido", "Estrela", "Delta", "Bifásico", "Monofásico", "Série/paralelo", "Delta aterrado"};
                dadosCSV.append("Tipo de Ligação;" + items[dadosCabecalhoQEE.tipoLigacao] + "\n");
                dadosCSV.append("Data;Hora;VA;VB;VC;Status\n");
                String format = "%s;%s;%.2f;%.2f;%.2f;%s;\n";

                dadosCSVCompleto.append("NS do medidor;" + respostaAbsoluto.getNumeroMedidor() + "\n");
                dadosCSVCompleto.append("Data/Hora de Início;" + dadosCabecalhoQEE.textdataInicio + "\n");
                dadosCSVCompleto.append("Data/Hora de Final;" + dadosCabecalhoQEE.textdataFim + "\n");
                dadosCSVCompleto.append("Numero de Registros;" + dadosCabecalhoQEE.numeroRegistros + "\n");
                dadosCSVCompleto.append("Numero de Registros Válidos;" + dadosCabecalhoQEE.numeroRegistrosValidos + "\n");
                //dadosCSVCompleto.append("Intervalo da memoria QEE;" + String.valueOf(dadosCabecalhoQEE.intevaloQEE) + "\n");
                dadosCSVCompleto.append("Intervalo da memoria QEE;10\n");
                dadosCSVCompleto.append("Tensão de Referência;" + String.format("%.2f", dadosCabecalhoQEE.tensaoReferencia) + "\n");
                dadosCSVCompleto.append("Percentual para a tensão precária superior;" + String.format(br, "%.2f", dadosCabecalhoQEE.percentualTensaoPrecariaSuperior) + "\n");
                dadosCSVCompleto.append("Percentual para a tensão precária inferior;" + String.format(br, "%.2f", dadosCabecalhoQEE.percentualTensaoPrecariaInferior) + "\n");
                dadosCSVCompleto.append("Percentual para a tensão crítica superior;" + String.format(br, "%.2f", dadosCabecalhoQEE.percentualTensaoCriticaSuperior) + "\n");
                dadosCSVCompleto.append("Percentual para a tensão crítica inferior;" + String.format(br, "%.2f", dadosCabecalhoQEE.percentualTensaoCriticaInferior) + "\n");
                dadosCSVCompleto.append("DRP(%);" + String.format(br, "%.2f", dadosCabecalhoQEE.DRP) + "\n");
                dadosCSVCompleto.append("DRC(%);" + String.format(br, "%.2f", dadosCabecalhoQEE.DRC) + "\n");
                dadosCSVCompleto.append("DTT95%;" + String.format(br, "%.2f", dadosCabecalhoQEE.DTT95) + "\n");
                dadosCSVCompleto.append("FD95%;" + String.format(br, "%.2f", dadosCabecalhoQEE.FD95) + "\n");
                dadosCSVCompleto.append("Tipo de Ligação;" + items[dadosCabecalhoQEE.tipoLigacao] + "\n");
                String formatCompleto = "";
                if((dadosCabecalhoQEE.grandezas & 0xFF) == 0xD7){
                    dadosCSVCompleto.append("Data;Hora;VA;VB;VC;Desequilíbio;DHTA(%);DHTB(%);DHTC(%);FPA(%);FPB(%);FPC(%);FP3(%);f(Hz);T(°C);VTCDM;VTCDT;Varf;Interrupções;Status\n");

                    formatCompleto = "%s;%s;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%d;%d;%d;%d;%s;\n";
                }
                else if((dadosCabecalhoQEE.grandezas & 0xFF) == 0xD5){
                    dadosCSVCompleto.append("Data;Hora;VA;VB;VC;Desequilíbio;DHTA(%);DHTB(%);DHTC(%);f(Hz);T(°C);VTCDM;VTCDT;Varf;Interrupções;Status\n");

                    formatCompleto = "%s;%s;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%d;%d;%d;%d;%s;\n";
                }

                Calendar local_date = dadosCabecalhoQEE.dataInicio;
                // vou plotar os resultados...
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

                if((dadosCabecalhoQEE.grandezas & 0xFF) == 0xD7) {
                    for (int i = 0; i < processaQEE.mDadosQEE.size(); i++) {
                        RespostaAbsoluto.DadosQEE dados = processaQEE.mDadosQEE.get(i);
                        local_date.add(Calendar.MINUTE, 10);
                        String data = dateFormat.format(local_date.getTime());
                        String hora = timeFormat.format(local_date.getTime());
                        String debug = "###### ITEM: " + i + " > Data: " + data + " " + hora + "\r\n" +
                                String.format(br, "Eventos......... %d | %d | %d | %d | %d", dados.status, dados.VTCDMomentaneos, dados.VTCDTemporarios, dados.variacoesFrequencia, dados.interrupcoes) + "\r\n" +
                                String.format(br, "Frequencia...... %d | %.2f", dados.frequencia, (float) dados.frequencia * dadosCabecalhoQEE.constanteMultiplicacaoFrequencia) + "\r\n" +
                                String.format(br, "Temperatura..... %d | %.2f", dados.temperatura, (float) dados.temperatura * dadosCabecalhoQEE.constanteMultiplicacaoTemperatura) + "\r\n" +
                                String.format(br, "Tensa A......... %d | %.2f", dados.tensaoA, (float) dados.tensaoA * dadosCabecalhoQEE.constanteMultiplicacaoTensao) + "\r\n" +
                                String.format(br, "Tensa B......... %d | %.2f", dados.tensaoB, (float) dados.tensaoB * dadosCabecalhoQEE.constanteMultiplicacaoTensao) + "\r\n" +
                                String.format(br, "Tensa C......... %d | %.2f", dados.tensaoC, (float) dados.tensaoC * dadosCabecalhoQEE.constanteMultiplicacaoTensao) + "\r\n" +
                                String.format(br, "Desequilibrio... %d | %.2f", dados.desequilibrio, (float) dados.desequilibrio * dadosCabecalhoQEE.constanteMultiplicacaoDesequilibrio) + "\r\n" +
                                String.format(br, "DHTA............ %d | %.2f", dados.DHTA, (float) dados.DHTA * dadosCabecalhoQEE.constanteMultiplicacaoHarmonicas) + "\r\n" +
                                String.format(br, "DHTB............ %d | %.2f", dados.DHTB, (float) dados.DHTB * dadosCabecalhoQEE.constanteMultiplicacaoHarmonicas) + "\r\n" +
                                String.format(br, "DHTC............ %d | %.2f", dados.DHTC, (float) dados.DHTC * dadosCabecalhoQEE.constanteMultiplicacaoHarmonicas) + "\r\n" +
                                String.format(br, "Fat. PotA....... %d | %.2f", dados.fatPotenciaA, (float) dados.fatPotenciaA * dadosCabecalhoQEE.constanteMultiplicacaoFatorPotencia) + "\r\n" +
                                String.format(br, "Fat. PotB....... %d | %.2f", dados.fatPotenciaB, (float) dados.fatPotenciaB * dadosCabecalhoQEE.constanteMultiplicacaoFatorPotencia) + "\r\n" +
                                String.format(br, "Fat. PotC....... %d | %.2f", dados.fatPotenciaC, (float) dados.fatPotenciaC * dadosCabecalhoQEE.constanteMultiplicacaoFatorPotencia) + "\r\n" +
                                String.format(br, "Fat. Pot.Trif... %d | %.2f", dados.fatPotenciaTrifasico, (float) dados.fatPotenciaTrifasico * dadosCabecalhoQEE.constanteMultiplicacaoFatorPotencia);

                        Log.i(TAG, debug);

                        String status = (dados.status > 0) ? "Inválido" : "Válido";
                        dadosCSV.append(
                                String.format(format, data, hora,
                                        (float) dados.tensaoA * dadosCabecalhoQEE.constanteMultiplicacaoTensao,
                                        (float) dados.tensaoB * dadosCabecalhoQEE.constanteMultiplicacaoTensao,
                                        (float) dados.tensaoC * dadosCabecalhoQEE.constanteMultiplicacaoTensao,
                                        status));
                        dadosCSVCompleto.append(
                                String.format(formatCompleto, data, hora,
                                        (float) dados.tensaoA * dadosCabecalhoQEE.constanteMultiplicacaoTensao,
                                        (float) dados.tensaoB * dadosCabecalhoQEE.constanteMultiplicacaoTensao,
                                        (float) dados.tensaoC * dadosCabecalhoQEE.constanteMultiplicacaoTensao,
                                        (float) dados.desequilibrio * dadosCabecalhoQEE.constanteMultiplicacaoDesequilibrio,
                                        (float) dados.DHTA * dadosCabecalhoQEE.constanteMultiplicacaoHarmonicas,
                                        (float) dados.DHTB * dadosCabecalhoQEE.constanteMultiplicacaoHarmonicas,
                                        (float) dados.DHTC * dadosCabecalhoQEE.constanteMultiplicacaoHarmonicas,
                                        (float) dados.fatPotenciaA * dadosCabecalhoQEE.constanteMultiplicacaoFatorPotencia,
                                        (float) dados.fatPotenciaB * dadosCabecalhoQEE.constanteMultiplicacaoFatorPotencia,
                                        (float) dados.fatPotenciaC * dadosCabecalhoQEE.constanteMultiplicacaoFatorPotencia,
                                        (float) dados.fatPotenciaTrifasico * dadosCabecalhoQEE.constanteMultiplicacaoFatorPotencia,
                                        (float) dados.frequencia * dadosCabecalhoQEE.constanteMultiplicacaoFrequencia,
                                        (float) dados.temperatura * dadosCabecalhoQEE.constanteMultiplicacaoTemperatura,
                                        dados.VTCDMomentaneos,
                                        dados.VTCDTemporarios,
                                        dados.variacoesFrequencia,
                                        dados.interrupcoes,
                                        status));
                    }
                }
                else if((dadosCabecalhoQEE.grandezas & 0xFF) == 0xD5){
                    for (int i = 0; i < processaQEESM2.mDadosQEESM2.size(); i++) {
                        RespostaAbsoluto.DadosQEESM2 dadosSM2 = processaQEESM2.mDadosQEESM2.get(i);
                        local_date.add(Calendar.MINUTE, 10);
                        String data = dateFormat.format(local_date.getTime());
                        String hora = timeFormat.format(local_date.getTime());
                        String debug = "###### ITEM: " + i + " > Data: " + data + " " + hora + "\r\n" +
                                String.format(br, "Eventos......... %d | %d | %d | %d | %d", dadosSM2.status, dadosSM2.VTCDMomentaneos, dadosSM2.VTCDTemporarios, dadosSM2.variacoesFrequencia, dadosSM2.interrupcoes) + "\r\n" +
                                String.format(br, "Frequencia...... %d | %.2f", dadosSM2.frequencia, (float) dadosSM2.frequencia * dadosCabecalhoQEE.constanteMultiplicacaoFrequencia) + "\r\n" +
                                String.format(br, "Temperatura..... %d | %.2f", dadosSM2.temperatura, (float) dadosSM2.temperatura * dadosCabecalhoQEE.constanteMultiplicacaoTemperatura) + "\r\n" +
                                String.format(br, "Tensa A......... %d | %.2f", dadosSM2.tensaoA, (float) dadosSM2.tensaoA * dadosCabecalhoQEE.constanteMultiplicacaoTensao) + "\r\n" +
                                String.format(br, "Tensa B......... %d | %.2f", dadosSM2.tensaoB, (float) dadosSM2.tensaoB * dadosCabecalhoQEE.constanteMultiplicacaoTensao) + "\r\n" +
                                String.format(br, "Tensa C......... %d | %.2f", dadosSM2.tensaoC, (float) dadosSM2.tensaoC * dadosCabecalhoQEE.constanteMultiplicacaoTensao) + "\r\n" +
                                String.format(br, "Desequilibrio... %d | %.2f", dadosSM2.desequilibrio, (float) dadosSM2.desequilibrio * dadosCabecalhoQEE.constanteMultiplicacaoDesequilibrio) + "\r\n" +
                                String.format(br, "DHTA............ %d | %.2f", dadosSM2.DHTA, (float) dadosSM2.DHTA * dadosCabecalhoQEE.constanteMultiplicacaoHarmonicas) + "\r\n" +
                                String.format(br, "DHTB............ %d | %.2f", dadosSM2.DHTB, (float) dadosSM2.DHTB * dadosCabecalhoQEE.constanteMultiplicacaoHarmonicas) + "\r\n" +
                                String.format(br, "DHTC............ %d | %.2f", dadosSM2.DHTC, (float) dadosSM2.DHTC * dadosCabecalhoQEE.constanteMultiplicacaoHarmonicas);

                        Log.i(TAG, debug);

                        String status = (dadosSM2.status > 0) ? "Inválido" : "Válido";
                        dadosCSV.append(
                                String.format(format, data, hora,
                                        (float) dadosSM2.tensaoA * dadosCabecalhoQEE.constanteMultiplicacaoTensao,
                                        (float) dadosSM2.tensaoB * dadosCabecalhoQEE.constanteMultiplicacaoTensao,
                                        (float) dadosSM2.tensaoC * dadosCabecalhoQEE.constanteMultiplicacaoTensao,
                                        status));
                        dadosCSVCompleto.append(
                                String.format(formatCompleto, data, hora,
                                        (float) dadosSM2.tensaoA * dadosCabecalhoQEE.constanteMultiplicacaoTensao,
                                        (float) dadosSM2.tensaoB * dadosCabecalhoQEE.constanteMultiplicacaoTensao,
                                        (float) dadosSM2.tensaoC * dadosCabecalhoQEE.constanteMultiplicacaoTensao,
                                        (float) dadosSM2.desequilibrio * dadosCabecalhoQEE.constanteMultiplicacaoDesequilibrio,
                                        (float) dadosSM2.DHTA * dadosCabecalhoQEE.constanteMultiplicacaoHarmonicas,
                                        (float) dadosSM2.DHTB * dadosCabecalhoQEE.constanteMultiplicacaoHarmonicas,
                                        (float) dadosSM2.DHTC * dadosCabecalhoQEE.constanteMultiplicacaoHarmonicas,
                                        (float) dadosSM2.frequencia * dadosCabecalhoQEE.constanteMultiplicacaoFrequencia,
                                        (float) dadosSM2.temperatura * dadosCabecalhoQEE.constanteMultiplicacaoTemperatura,
                                        dadosSM2.VTCDMomentaneos,
                                        dadosSM2.VTCDTemporarios,
                                        dadosSM2.variacoesFrequencia,
                                        dadosSM2.interrupcoes,
                                        status));
                    }
                }

                String nomeArquivo = String.format("MemoriaQEETensao_%s_%s_%s.csv", respostaAbsoluto.getNumeroMedidor(), mDeviceName.substring(4).trim(),
                        new SimpleDateFormat("ddMMyyyy'_'HHmmss").format(Calendar.getInstance().getTime()));

                String nomeArquivoCompleto = String.format("MemoriaQEE_%s_%s_%s.csv", respostaAbsoluto.getNumeroMedidor(), mDeviceName.substring(4).trim(),
                        new SimpleDateFormat("ddMMyyyy'_'HHmmss").format(Calendar.getInstance().getTime()));

                if (!Arquivo.salvarArquivo(this, nomeArquivo, dadosCSV.toString())) {
                    Toast.makeText(this, "Erro ao salvar arquivo da memória QEE",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Arquivo de QEE salvo com sucesso:\n" + nomeArquivo,
                            Toast.LENGTH_LONG).show();
                }

                if (!Arquivo.salvarArquivo(this, nomeArquivoCompleto, dadosCSVCompleto.toString())) {
                    Toast.makeText(this, "Erro ao salvar arquivo completo da memória QEE",
                            Toast.LENGTH_SHORT).show();
                } else {

                }

                progressDialog.hide();
                progressDialog.dismiss();
                mHandler.removeCallbacksAndMessages(null);
            }
            //dadosAbnt51 = respostaAbsoluto.interpretaResposta51();
        }
    }

    private void processarRespostaAB52(RespostaAbsoluto respostaAbsoluto) {
        Log.d(TAG, "## Resposta AB52 ...");
        byte pacote = 1;
        if (funcaoEmExecucao == TipoOperacao.InicioMemoriaMassaSM) {
            funcaoEmExecucao = TipoOperacao.MemoriaMassaSM;
            dadosCabecalhoMMSMBloco1 = respostaAbsoluto.interpretaRespostaAB52CabecalhoBloco1();
            mRespostaMMSM = new byte[247];
            progressDialog = AlertBuilder.createProgressDialog(this, "Enviando pacote " + dadosCabecalhoMMSMBloco1.pacoteAtual + " de " + dadosCabecalhoMMSMBloco1.numeroPacotes);
            progressDialog.show();
            showTimeOutMM();
            enviaComandoAB52(pacote, (byte) 0x05, IntervaloDiasMMSM, MetodoNumeroSerial);
        } else {
            dadosCabecalhoMMSM = respostaAbsoluto.interpretaRespostaAB52Cabecalho();
            int tamanho = (respostaAbsoluto.mData.length - 11);
            if ((tamanho + dadosCabecalhoMMSMBloco1.posicaoAtual) > mRespostaMMSM.length) {
                tamanho = mRespostaMMSM.length - dadosCabecalhoMMSMBloco1.posicaoAtual;
            }
            System.arraycopy(respostaAbsoluto.getData(), 9, mRespostaMMSM, dadosCabecalhoMMSMBloco1.posicaoAtual, tamanho);
            dadosCabecalhoMMSMBloco1.posicaoAtual += tamanho;
            //mRespostaComposta.add(respostaAbsoluto.getData());
            if (!respostaAbsoluto.interpretaRespostaAB52()) {
                dadosCabecalhoMMSMBloco1.pacoteAtual++;
                progressDialog.setMessage("Enviando pacote " + dadosCabecalhoMMSMBloco1.pacoteAtual + " de " + dadosCabecalhoMMSMBloco1.numeroPacotes);
                showTimeOutMM();
                enviaComandoAB52(pacote, (byte) 0x05, IntervaloDiasMMSM, MetodoNumeroSerial);
            } else {
                mMMRunnable = null;
                mHandler.removeCallbacksAndMessages(null);
                progressDialog.setMessage("Aguarde, Processando MM SM.");
                processaMMSM = respostaAbsoluto.preparaMMSM();
                //long numero_registros = dadosCabecalhoMMSM.numeroRegistros;
                for (int j = 0; j < mRespostaQEE.length; j += 31) {
                    processaMMSM.preencheDados(Arrays.copyOfRange(mRespostaMMSM, j, j + 31));
                }
                //Log.i(TAG, "Data de Inicio: " + dadosCabecalhoMMSM.textdataInicio + " | " + dadosCabecalhoMMSM.textdataFim);

                Locale br = Locale.forLanguageTag("BR");

                StringBuilder dadosCSV = new StringBuilder();
                StringBuilder dadosCSVCompleto = new StringBuilder();
                dadosCSV.append("StarMeasure\n");
                dadosCSV.append("Ponto;" + mDeviceName.substring(4) + "\n");
                dadosCSV.append("Area;" + "\n");
                dadosCSV.append("Numero de Registros;" + dadosCabecalhoMMSMBloco1.tamanhoEstrutura + "\n");
                dadosCSV.append("Intervalo da memoria SM;10\n");
                //dadosCSV.append("Data/Hora de Início;" + dadosCabecalhoMMSM.textdataInicio + "\n");
                //dadosCSV.append("Data/Hora de Final;" + dadosCabecalhoMMSM.textdataFim + "\n");
                dadosCSV.append("TP Original;1\n");
                dadosCSV.append("TP;1\n");

                String[] items = new String[]{"Indefinido", "Estrela", "Delta", "Bifásico", "Monofásico", "Série/paralelo", "Delta aterrado"};
                //dadosCSV.append("Tipo de Ligação;" + items[dadosCabecalhoMMSM.tipoLigacao] + "\n");
                dadosCSV.append("Data;Hora;VA;VB;VC;Status\n");
                String format = "%s;%s;%.2f;%.2f;%.2f;%s;\n";

                dadosCSVCompleto.append("NS do medidor;" + respostaAbsoluto.getNumeroMedidor() + "\n");
                //dadosCSVCompleto.append("Data/Hora de Início;" + dadosCabecalhoMMSM.textdataInicio + "\n");
                //dadosCSVCompleto.append("Data/Hora de Final;" + dadosCabecalhoMMSM.textdataFim + "\n");
                dadosCSVCompleto.append("Numero de Registros;" + dadosCabecalhoMMSMBloco1.tamanhoEstrutura + "\n");
                //dadosCSVCompleto.append("Numero de Registros Válidos;" + dadosCabecalhoMMSM.numeroRegistrosValidos + "\n");
                //dadosCSVCompleto.append("Intervalo da memoria SM;" + String.valueOf(dadosCabecalhoMMSM.intevaloQEE) + "\n");
                dadosCSVCompleto.append("Intervalo da memoria SM;10\n");
                dadosCSVCompleto.append("Tensão de Referência;" + String.format("%.2f", dadosCabecalhoQEE.tensaoReferencia) + "\n");
                //dadosCSVCompleto.append("Percentual para a tensão precária superior;" + String.format(br, "%.2f", dadosCabecalhoMMSM.percentualTensaoPrecariaSuperior) + "\n");
                //dadosCSVCompleto.append("Percentual para a tensão precária inferior;" + String.format(br, "%.2f", dadosCabecalhoMMSM.percentualTensaoPrecariaInferior) + "\n");
                //dadosCSVCompleto.append("Percentual para a tensão crítica superior;" + String.format(br, "%.2f", dadosCabecalhoMMSM.percentualTensaoCriticaSuperior) + "\n");
                //dadosCSVCompleto.append("Percentual para a tensão crítica inferior;" + String.format(br, "%.2f", dadosCabecalhoMMSM.percentualTensaoCriticaInferior) + "\n");
                //dadosCSVCompleto.append("DRP(%);" + String.format(br, "%.2f", dadosCabecalhoMMSM.DRP) + "\n");
                //dadosCSVCompleto.append("DRC(%);" + String.format(br, "%.2f", dadosCabecalhoMMSM.DRC) + "\n");
                //dadosCSVCompleto.append("DTT95%;" + String.format(br, "%.2f", dadosCabecalhoMMSM.DTT95) + "\n");
                //dadosCSVCompleto.append("FD95%;" + String.format(br, "%.2f", dadosCabecalhoMMSM.FD95) + "\n");
                //dadosCSVCompleto.append("Tipo de Ligação;" + items[dadosCabecalhoMMSM.tipoLigacao] + "\n");
                dadosCSVCompleto.append("Data;Hora;VA;VB;VC;Desequilíbio;DHTA(%);DHTB(%);DHTC(%);FPA(%);FPB(%);FPC(%);FP3(%);f(Hz);T(°C);VTCDM;VTCDT;Varf;Interrupções;Status\n");

                String formatCompleto = "%s;%s;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%d;%d;%d;%d;%s;\n";


                //Calendar local_date = dadosCabecalhoMMSM.dataInicio;
                // vou plotar os resultados...
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

                for (int i = 0; i < processaQEE.mDadosQEE.size(); i++) {
                    RespostaAbsoluto.DadosQEE dados = processaMMSM.mDadosQEE.get(i);
                    //local_date.add(Calendar.MINUTE, 10);
                    //String data = dateFormat.format(local_date.getTime());
                    //String hora = timeFormat.format(local_date.getTime());
                    //String debug = "###### ITEM: " + i + " > Data: " + data + " " + hora + "\r\n" +
                    //        String.format(br, "Eventos......... %d | %d | %d | %d | %d", dados.status, dados.VTCDMomentaneos, dados.VTCDTemporarios, dados.variacoesFrequencia, dados.interrupcoes) + "\r\n" +
                    //        String.format(br, "Frequencia...... %d | %.2f", dados.frequencia, (float) dados.frequencia * dadosCabecalhoMMSM.constanteMultiplicacaoFrequencia) + "\r\n" +
                    //        String.format(br, "Temperatura..... %d | %.2f", dados.temperatura, (float) dados.temperatura * dadosCabecalhoMMSM.constanteMultiplicacaoTemperatura) + "\r\n" +
                    //        String.format(br, "Tensa A......... %d | %.2f", dados.tensaoA, (float) dados.tensaoA * dadosCabecalhoMMSM.constanteMultiplicacaoTensao) + "\r\n" +
                    //        String.format(br, "Tensa B......... %d | %.2f", dados.tensaoB, (float) dados.tensaoB * dadosCabecalhoMMSM.constanteMultiplicacaoTensao) + "\r\n" +
                    //        String.format(br, "Tensa C......... %d | %.2f", dados.tensaoC, (float) dados.tensaoC * dadosCabecalhoMMSM.constanteMultiplicacaoTensao) + "\r\n" +
                    //        String.format(br, "Desequilibrio... %d | %.2f", dados.desequilibrio, (float) dados.desequilibrio * dadosCabecalhoMMSM.constanteMultiplicacaoDesequilibrio) + "\r\n" +
                    //        String.format(br, "DHTA............ %d | %.2f", dados.DHTA, (float) dados.DHTA * dadosCabecalhoMMSM.constanteMultiplicacaoHarmonicas) + "\r\n" +
                    //        String.format(br, "DHTB............ %d | %.2f", dados.DHTB, (float) dados.DHTB * dadosCabecalhoMMSM.constanteMultiplicacaoHarmonicas) + "\r\n" +
                    //        String.format(br, "DHTC............ %d | %.2f", dados.DHTC, (float) dados.DHTC * dadosCabecalhoMMSM.constanteMultiplicacaoHarmonicas) + "\r\n" +
                    //        String.format(br, "Fat. PotA....... %d | %.2f", dados.fatPotenciaA, (float) dados.fatPotenciaA * dadosCabecalhoMMSM.constanteMultiplicacaoFatorPotencia) + "\r\n" +
                    //        String.format(br, "Fat. PotB....... %d | %.2f", dados.fatPotenciaA, (float) dados.fatPotenciaA * dadosCabecalhoMMSM.constanteMultiplicacaoFatorPotencia) + "\r\n" +
                    //        String.format(br, "Fat. PotC....... %d | %.2f", dados.fatPotenciaA, (float) dados.fatPotenciaA * dadosCabecalhoMMSM.constanteMultiplicacaoFatorPotencia) + "\r\n" +
                    //        String.format(br, "Fat. Pot.Trif... %d | %.2f", dados.fatPotenciaTrifasico, (float) dados.fatPotenciaTrifasico * dadosCabecalhoMMSM.constanteMultiplicacaoFatorPotencia);

                    //Log.i(TAG, debug);

                    String status = (dados.status > 0) ? "Inválido" : "Válido";
                    //dadosCSV.append(
                    //        String.format(format, data, hora,
                    //                (float) dados.tensaoA * dadosCabecalhoMMSM.constanteMultiplicacaoTensao,
                    //                (float) dados.tensaoB * dadosCabecalhoMMSM.constanteMultiplicacaoTensao,
                    //                (float) dados.tensaoC * dadosCabecalhoMMSM.constanteMultiplicacaoTensao,
                    //                status));
                    //dadosCSVCompleto.append(
                    //        String.format(formatCompleto, data, hora,
                    //                (float) dados.tensaoA * dadosCabecalhoMMSM.constanteMultiplicacaoTensao,
                    //                (float) dados.tensaoB * dadosCabecalhoMMSM.constanteMultiplicacaoTensao,
                    //                (float) dados.tensaoC * dadosCabecalhoMMSM.constanteMultiplicacaoTensao,
                    //                (float) dados.desequilibrio * dadosCabecalhoMMSM.constanteMultiplicacaoDesequilibrio,
                    //                (float) dados.DHTA * dadosCabecalhoMMSM.constanteMultiplicacaoHarmonicas,
                    //                (float) dados.DHTB * dadosCabecalhoMMSM.constanteMultiplicacaoHarmonicas,
                    //                (float) dados.DHTC * dadosCabecalhoMMSM.constanteMultiplicacaoHarmonicas,
                    //                (float) dados.fatPotenciaA * dadosCabecalhoMMSM.constanteMultiplicacaoFatorPotencia,
                    //                (float) dados.fatPotenciaB * dadosCabecalhoMMSM.constanteMultiplicacaoFatorPotencia,
                    //                (float) dados.fatPotenciaC * dadosCabecalhoMMSM.constanteMultiplicacaoFatorPotencia,
                    //                (float) dados.fatPotenciaTrifasico * dadosCabecalhoMMSM.constanteMultiplicacaoFatorPotencia,
                    //                (float) dados.frequencia * dadosCabecalhoMMSM.constanteMultiplicacaoFrequencia,
                    //                (float) dados.temperatura * dadosCabecalhoMMSM.constanteMultiplicacaoTemperatura,
                    //                dados.VTCDMomentaneos,
                    //                dados.VTCDTemporarios,
                    //                dados.variacoesFrequencia,
                    //                dados.interrupcoes,
                    //                status));
                }

                String nomeArquivo = String.format("MemoriaQEETensao_%s_%s_%s.csv", respostaAbsoluto.getNumeroMedidor(), mDeviceName.substring(4).trim(),
                        new SimpleDateFormat("ddMMyyyy'_'HHmmss").format(Calendar.getInstance().getTime()));

                String nomeArquivoCompleto = String.format("MemoriaQEE_%s_%s_%s.csv", respostaAbsoluto.getNumeroMedidor(), mDeviceName.substring(4).trim(),
                        new SimpleDateFormat("ddMMyyyy'_'HHmmss").format(Calendar.getInstance().getTime()));

                if (!Arquivo.salvarArquivo(this, nomeArquivo, dadosCSV.toString())) {
                    Toast.makeText(this, "Erro ao salvar arquivo da memória QEE",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Arquivo de QEE salvo com sucesso:\n" + nomeArquivo,
                            Toast.LENGTH_LONG).show();
                }

                if (!Arquivo.salvarArquivo(this, nomeArquivoCompleto, dadosCSVCompleto.toString())) {
                    Toast.makeText(this, "Erro ao salvar arquivo completo da memória QEE",
                            Toast.LENGTH_SHORT).show();
                } else {

                }

                progressDialog.hide();
                progressDialog.dismiss();
                mHandler.removeCallbacksAndMessages(null);
            }
            //dadosAbnt51 = respostaAbsoluto.interpretaResposta51();
        }
    }

    private void enviarQEEParametrizacao(RespostaAbsoluto respostaAbsoluto, String
            numeroMedidor) {

        dadosCabecalhoQEE = respostaAbsoluto.interpretaRespostaAB11Cabecalho();

        funcaoEmExecucao = TipoOperacao.FIM;
        final View conf_qee = getLayoutInflater().inflate(R.layout.conf_qee, null);
        final EditText tensao = conf_qee.findViewById(R.id.tensao_nominal);
        tensao.setText(String.format("%.2f", dadosCabecalhoQEE.tensaoReferencia));

        final Spinner tipo_ligacao = conf_qee.findViewById(R.id.tipo_ligacao);
        String[] items = new String[]{"Monofásico", "Bifásico", "Estrela", "Delta aterrado"};
        if (tipo_medidor == TipoMedidor.EASY_VOLT) {
            items = new String[]{"Monofásico", "Bifásico", "Estrela"};
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipo_ligacao.setAdapter(adapter);

        if (dadosCabecalhoQEE.tipoLigacao == 4) {
            tipo_ligacao.setSelection(0);
        } else if (dadosCabecalhoQEE.tipoLigacao == 3) {
            tipo_ligacao.setSelection(1);
        } else if (dadosCabecalhoQEE.tipoLigacao == 1) {
            tipo_ligacao.setSelection(2);
        } else if (dadosCabecalhoQEE.tipoLigacao == 6) {
            tipo_ligacao.setSelection(3);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                android.R.style.Theme_Material_Dialog_Alert);
        AlertDialog alertDialog = builder
                .setTitle("Parametrização da QEE")
                .setMessage("Tipo de ligação atual: " + traduzTipoLigacaoIntToString(dadosCabecalhoQEE.tipoLigacao))
                .setView(conf_qee)
                .setPositiveButton("OK", (dialog, which) -> {
                    if (tensao.getText().toString().isEmpty()) {
                        tensao.setText("0");
                    }
                    int v_tensao = Integer.parseInt(tensao.getText().toString());
                    codigoCanal = traduzTipoLigacaoStringToInt(tipo_ligacao.getSelectedItem().toString());
                    if (v_tensao >= 0) {
                        String num_medidor = "";

                        byte[] comando = new ComandoAbsoluto.ABNT9895()
                                .comMedidorNumero(num_medidor)
                                .build(v_tensao);
                        enviarComando(comando);
                        Log.i("amigo->", "Amigo estou aqui!\n" + "Voltagem: " + v_tensao + "\n" + Util.ByteArrayToHexString(comando));

                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        if (imm != null)
                            imm.hideSoftInputFromWindow(conf_qee.getWindowToken(), 0);
                    } else {
                        Toast.makeText(DeviceActivity.super.getApplicationContext(),
                                "Informe a tensão nominal de 80 a 280.",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    if (imm != null)
                        imm.hideSoftInputFromWindow(conf_qee.getWindowToken(), 0);
                    // do nothing
                }).create();


        alertDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        alertDialog.show();
    }

    @Contract(pure = true)
    private int traduzTipoLigacaoStringToInt(String tipoLigacao) {
        int i = 0;

        switch (tipoLigacao) {
            case "Estrela":
                i = 1;
                break;
            case "Delta":
                i = 2;
                break;
            case "Bifásico":
                i = 3;
                break;
            case "Monofásico":
                i = 4;
                break;
            case "Série/paralelo":
                i = 5;
                break;
            case "Delta aterrado":
                i = 6;
                break;
        }
        return i;
    }

    @Contract(pure = true)
    private String traduzTipoLigacaoIntToString(int tipoLigacao) {
        String s = "Indefinido";

        switch (tipoLigacao) {
            case 1:
                s = "Estrela";
                break;
            case 2:
                s = "Delta";
                break;
            case 3:
                s = "Bifásico";
                break;
            case 4:
                s = "Monofásico";
                break;
            case 5:
                s = "Série/paralelo";
                break;
            case 6:
                s = "Delta aterrado";
                break;
        }
        return s;
    }

    private void processarRespostaOcorrencia(RespostaAbsoluto respostaAbsoluto) {
        Toast.makeText(DeviceActivity.this,
                getString(R.string.text_ocorrencia_medidor),
                Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Ocorrência: " + respostaAbsoluto.toString());
        enviarLimparOcorrencia(respostaAbsoluto.getSufixoNumeroMedidor(), respostaAbsoluto.getCodigoOcorrencia(), MetodoNumeroSerial);
    }

    private void processarRespostaConfiguracaoMedidorEasy(RespostaAbsoluto respostaAbsoluto) {

        ComandoAbsoluto.LeituraConfiguracaoMedidor mLeitura = respostaAbsoluto.interpretaConfiguracaoMedidor();

        iItem = mLeitura.Item;

        byte[] data = respostaAbsoluto.getData();
        iuMedidor = new byte[]{data[15], data[16], data[17], data[18], data[19]};
        Log.i(TAG, "mIu: " + Util.ByteArrayToHexString(iuMedidor));
        enviarLeitura87();
    }

    private void processarRespostaConfiguracaoMedidorAbsoluto(RespostaAbsoluto respostaAbsoluto) {
        byte[] data = respostaAbsoluto.getData();

        ComandoAbsoluto.LeituraConfiguracaoMedidor mLeitura = respostaAbsoluto.interpretaConfiguracaoMedidor();

        iItem = mLeitura.Item;

        iuMedidor = new byte[]{data[16], data[17], data[18], data[19], data[20]};
        Log.i(TAG, "mIu: " + Util.ByteArrayToHexString(iuMedidor));

        medidorSemRele = (respostaAbsoluto.mData[22] == 0);


        if (medidorSemRele) {
            for (MedidorAbsoluto medidor : medidores) {
                medidor.status = 0x01;
                medidor.semRele = true;
            }
        }

        mMeterListAdapter = new MeterListAdapter(medidores);
        //mRecyclerView.setAdapter(mMeterListAdapter);
        mMeterListAdapter.notifyDataSetChanged();

        //enviarLeituraParametrosHospedeiro();

        if(MetodoNumeroSerial == 0x02){
            mAB14 = new ComandoAbsoluto.AB14().comNumerMedidor("00000000", MetodoNumeroSerial);
            enviarAB14(MetodoNumeroSerial);
        }
        else{
            mMeterListAdapter = new MeterListAdapter(medidores);
            mRecyclerView.setAdapter(mMeterListAdapter);
            mMeterListAdapter.notifyDataSetChanged();
        }

        // if (!medidorSemRele) {
        //    enviarProximoStatusMedidor("99999999");
        //    animateRefresh();
        //}
    }

    private void processarRespostaLeituraParametrosHospedeiro(RespostaAbsoluto resposta) {

        if (leuMedidoresIndividuais)
            return;
        mRespostaComposta.add(resposta.getData());
        mTimeoutHandler.removeCallbacks(mMedidoresIndividuaisRunnable);
        leuMedidoresIndividuais = true;
        progressDialog.dismiss();
        mHandler.removeCallbacksAndMessages(null);
        RespostaAbsoluto respostaComposta = new RespostaAbsoluto(mRespostaComposta);
        medidores = respostaComposta.listaMedidores();
        medidores_ativos_1 = respostaComposta.mData[9] & 0xFF;
        medidores_ativos_2 = respostaComposta.mData[10] & 0xFF;
        medidores_ativos_3 = respostaComposta.mData[11] & 0xFF;
        medidores_ativos_4 = respostaComposta.mData[12] & 0xFF;
        medidores_ativos_5 = respostaComposta.mData[13] & 0xFF;
        medidores_ativos_6 = respostaComposta.mData[14] & 0xFF;
        medidores_ativos_7 = respostaComposta.mData[15] & 0xFF;
        medidores_ativos_8 = respostaComposta.mData[16] & 0xFF;
        medidores_ativos =  (long) (medidores_ativos_8) << 56;
        medidores_ativos |= (long) (medidores_ativos_7) << 48;
        medidores_ativos |= (long) (medidores_ativos_6) << 40;
        medidores_ativos |= (long) (medidores_ativos_5) << 32;
        medidores_ativos |= (long) (medidores_ativos_4) << 24;
        medidores_ativos |= (long) (medidores_ativos_3) << 16;
        medidores_ativos |= (long) (medidores_ativos_2) << 8;
        medidores_ativos |= (long) (medidores_ativos_1);

        enviarLeituraConfiguracao(tipo_medidor);

        //if(MetodoNumeroSerial == 0x02){
        //    mAB14 = new ComandoAbsoluto.AB14().comNumerMedidor("00000000", MetodoNumeroSerial);
        //    enviarAB14(MetodoNumeroSerial);
        //}
    }

    private void processarRespostaLeituraConfigParamModCom(@NonNull RespostaAbsoluto resposta) {
        byte[] data = resposta.getData();

        medidoresCom.numMedidor[0] = Util.ByteArrayToHexStringWS(new byte[]{data[7],data[8],data[9],data[10]});
        medidoresCom.numMedidor[1] = Util.ByteArrayToHexStringWS(new byte[]{data[11],data[12],data[13],data[14]});
        medidoresCom.numMedidor[2] = Util.ByteArrayToHexStringWS(new byte[]{data[15],data[16],data[17],data[18]});
        medidoresCom.numMedidor[3] = Util.ByteArrayToHexStringWS(new byte[]{data[19],data[20],data[21],data[22]});
        medidoresCom.numMedidor[4] = Util.ByteArrayToHexStringWS(new byte[]{data[23],data[24],data[25],data[26]});
        medidoresCom.numMedidor[5] = Util.ByteArrayToHexStringWS(new byte[]{data[27],data[28],data[29],data[30]});
        medidoresCom.numMedidor[6] = Util.ByteArrayToHexStringWS(new byte[]{data[31],data[32],data[33],data[34]});
        medidoresCom.numMedidor[7] = Util.ByteArrayToHexStringWS(new byte[]{data[35],data[36],data[37],data[38]});
        medidoresCom.numMedidor[8] = Util.ByteArrayToHexStringWS(new byte[]{data[39],data[40],data[41],data[42]});
        medidoresCom.numMedidor[9] = Util.ByteArrayToHexStringWS(new byte[]{data[43],data[44],data[45],data[46]});
        medidoresCom.numMedidor[10] = Util.ByteArrayToHexStringWS(new byte[]{data[47],data[48],data[49],data[50]});
        medidoresCom.numMedidor[11] = Util.ByteArrayToHexStringWS(new byte[]{data[51],data[52],data[53],data[54]});

        medidoresCom.metodoNum = data[127];
        medidoresCom.tensaoNominal = ByteArrayToFloat(data, 128);
        medidoresCom.calPO = data[132];

        if(MetodoNumeroSerial == 0x02){
            medidores.get(0).numero = medidores.get(0).numero.substring(0,8);
            medidores.get(0).numMedidor = Integer.valueOf(medidores.get(0).numero);
            for(int x=1, y=1; x < 35; x++){
                if(((medidores_ativos >> x) & 0x01) == 0x01){
                    if((x >= 1)&&(x <= 12)){
                        // Monofoásicos
                        medidores.get(y).numero = medidoresCom.numMedidor[x-1];
                        medidores.get(y).numMedidor = Integer.valueOf(medidores.get(y).numero);
                        medidores.get(y).tipo = x;
                    }
                    else if((x >= 13)&&(x <= 23)){
                        // Bifásicos
                        medidores.get(y).numero = medidoresCom.numMedidor[x-13];
                        medidores.get(y).numMedidor = Integer.valueOf(medidores.get(y).numero);
                        medidores.get(y).tipo = x;
                    }
                    else if(x >= 24){
                        // Trifásicos
                        medidores.get(y).numero = medidoresCom.numMedidor[x-24];
                        medidores.get(y).numMedidor = Integer.valueOf(medidores.get(y).numero);
                        medidores.get(y).tipo = x;
                    }

                    y++;
                    //if(medidores.get(x).fases == 1){
                    //    y+=1;
                    //}
                    //else if(medidores.get(x).fases == 2){
                    //    y+=2;
                    //}
                    //else if(medidores.get(x).fases == 3){
                    //    y+=3;
                    //}
                }
            }
        }

        Collections.sort(medidores, new Comparator<MedidorAbsoluto>() {
            public int compare(MedidorAbsoluto m1, MedidorAbsoluto m2) {
                return (int) (m1.numMedidor - m2.numMedidor);
            }
        });

        //for(int x=1; x < medidores.size(); x++){
        //    medidores.get(x).numero = medidoresCom.numMedidor[x-1];
        //    medidores.get(x).numMedidor = Integer.valueOf(medidores.get(x).numero);
        //    if(medidores.get(x).fases == 2){
        //        x+=1;
        //    }
        //    else if(medidores.get(x).fases == 3){
        //        x+=2;
        //    }
        //}
        if (medidorSemRele) {
            for (MedidorAbsoluto medidor : medidores) {
                medidor.status = 0x01;
                medidor.semRele = true;
            }
        }

        mMeterListAdapter = new MeterListAdapter(medidores);
        mRecyclerView.setAdapter(mMeterListAdapter);
        mMeterListAdapter.notifyDataSetChanged();

        if (!medidorSemRele) {
            enviarProximoStatusMedidor("99999999");
            animateRefresh();
        }
    }

    private void animateRefresh() {
        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate_refresh);
        rotation.setRepeatCount(Animation.INFINITE);
        mRefreshStatus.startAnimation(rotation);
    }

    private void processarRespostaLeituraStatusMedidor(RespostaAbsoluto respostaAbsoluto) {
        String numeroMedidor = "";
        if(MetodoNumeroSerial == 0x02){
            numeroMedidor = respostaAbsoluto.getNumeroMedidor();
        }
        else{
            numeroMedidor = respostaAbsoluto.getSufixoNumeroMedidor();
        }

        RespostaAbsoluto.LeituraStatusMedidor statusMedidor = respostaAbsoluto.interpretarLeituraStatusMedidor();
        Log.d(TAG, "LeituraStatusMedidor: " + numeroMedidor);
        boolean found = false;
        for (MedidorAbsoluto medidor : medidores) {
            Log.i("test->", medidor.toString());
            if (medidor.numero.endsWith(numeroMedidor)) {
                atualizarStatusMedidor(medidor, statusMedidor);
                NroMedidorReleComandado = medidor.numero;
                found = true;
            }
        }
        if (!found)
            atualizarStatusMedidor(medidores.get(0), statusMedidor);
        if (deveLerTudo)
            enviarProximoStatusMedidor(numeroMedidor);
    }

    private void enviaComando52(byte pacote) {
        String num_medidor = "";
        if (tipo_medidor == TipoMedidor.ABSOLUTO) {
            num_medidor = medidorSelecionado.numero;
        }
        byte[] comando = new ComandoAbsoluto.ABNT52()
                .comMedidorNumero(num_medidor)
                .build(pacote, (tipo_medidor == TipoMedidor.ABSOLUTO));
        enviarComando(comando);
    }

    private void processarRespostaAbnt51(RespostaAbsoluto respostaAbsoluto) {
        Log.d(TAG, "## Resposta 51 ...");
        dadosAbnt51 = respostaAbsoluto.interpretaResposta51();
        mRespostaComposta.clear();
        progressDialog = AlertBuilder.createProgressDialog(this, "Enviando pacote " + dadosAbnt51.pacoteAtual + " de " + dadosAbnt51.numeroPacotes);
        progressDialog.show();
        byte pacote = 0;
        enviaComando52(pacote);
    }

    private void processarRespostaAbnt52(RespostaAbsoluto respostaAbsoluto) {
        Log.d(TAG, "## Resposta 52: " + dadosAbnt51.pacoteAtual + " de " + dadosAbnt51.numeroPacotes);
        if (!respostaAbsoluto.interpretaResposta52()) {
            mRespostaComposta.add(respostaAbsoluto.getData());
            dadosAbnt51.pacoteAtual++;
            progressDialog.setMessage("Enviando pacote " + dadosAbnt51.pacoteAtual + " de " + dadosAbnt51.numeroPacotes);
            byte pacote = 1;
            showTimeOutMM();
            enviaComando52(pacote);
        } else {
            // agora processa a memoria de masssa...
            mMMRunnable = null;
            mHandler.removeCallbacksAndMessages(null);
            mRespostaComposta.add(respostaAbsoluto.getData());
            processa52 = respostaAbsoluto.prepara52();
            progressDialog.setMessage("Aguarde, Processando a Memória de Massa.");
            for (int i = 0; i < mRespostaComposta.size(); i++) {
                byte[] data = mRespostaComposta.get(i);
                int inicio = 7;
                if ((i % 3) == 0) {
                    while (inicio < 255) { // Começa no canal 1
                        long valor = Util.unsignedByteToInt(data[inicio]) + ((Util.unsignedByteToInt(data[inicio + 1]) & 0xF0) << 4);
                        processa52.mGrandezasC1.add((long) Util.unsignedByteToInt(data[inicio]) + ((Util.unsignedByteToInt(data[inicio + 1]) & 0xF0) << 4));
                        processa52.mGrandezasC2.add((long) Util.unsignedByteToInt(data[inicio + 2]) + ((Util.unsignedByteToInt(data[inicio + 1]) & 0x0F) << 8));
                        processa52.mGrandezasC3.add((long) Util.unsignedByteToInt(data[inicio + 3]) + ((Util.unsignedByteToInt(data[inicio + 4]) & 0xF0) << 4));

                        processa52.mGrandezasC1.add((long) Util.unsignedByteToInt(data[inicio + 5]) + ((Util.unsignedByteToInt(data[inicio + 4]) & 0x0F) << 8));
                        if (inicio != 250) {
                            processa52.mGrandezasC2.add((long) Util.unsignedByteToInt(data[inicio + 6]) + ((Util.unsignedByteToInt(data[inicio + 7]) & 0xF0) << 4));
                            processa52.mGrandezasC3.add((long) Util.unsignedByteToInt(data[inicio + 8]) + ((Util.unsignedByteToInt(data[inicio + 7]) & 0x0F) << 8));
                        }
                        inicio += 9;
                    }
                } else if ((i % 3) == 1) { // começa no canal 2
                    while (inicio < 255) {
                        processa52.mGrandezasC2.add((long) Util.unsignedByteToInt(data[inicio]) + ((Util.unsignedByteToInt(data[inicio + 1]) & 0xF0) << 4));
                        processa52.mGrandezasC3.add((long) Util.unsignedByteToInt(data[inicio + 2]) + ((Util.unsignedByteToInt(data[inicio + 1]) & 0x0F) << 8));
                        processa52.mGrandezasC1.add((long) Util.unsignedByteToInt(data[inicio + 3]) + ((Util.unsignedByteToInt(data[inicio + 4]) & 0xF0) << 4));

                        processa52.mGrandezasC2.add((long) Util.unsignedByteToInt(data[inicio + 5]) + ((Util.unsignedByteToInt(data[inicio + 4]) & 0x0F) << 8));
                        if (inicio != 250) {
                            processa52.mGrandezasC3.add((long) Util.unsignedByteToInt(data[inicio + 6]) + ((Util.unsignedByteToInt(data[inicio + 7]) & 0xF0) << 4));
                            processa52.mGrandezasC1.add((long) Util.unsignedByteToInt(data[inicio + 8]) + ((Util.unsignedByteToInt(data[inicio + 7]) & 0x0F) << 8));
                        }
                        inicio += 9;
                    }
                } else if ((i % 3) == 2) { // começa no canal 3
                    while (inicio < 255) {
                        processa52.mGrandezasC3.add((long) Util.unsignedByteToInt(data[inicio]) + ((Util.unsignedByteToInt(data[inicio + 1]) & 0xF0) << 4));
                        processa52.mGrandezasC1.add((long) Util.unsignedByteToInt(data[inicio + 2]) + ((Util.unsignedByteToInt(data[inicio + 1]) & 0x0F) << 8));
                        processa52.mGrandezasC2.add((long) Util.unsignedByteToInt(data[inicio + 3]) + ((Util.unsignedByteToInt(data[inicio + 4]) & 0xF0) << 4));

                        processa52.mGrandezasC3.add((long) Util.unsignedByteToInt(data[inicio + 5]) + ((Util.unsignedByteToInt(data[inicio + 4]) & 0x0F) << 8));
                        if (inicio != 250) {
                            processa52.mGrandezasC1.add((long) Util.unsignedByteToInt(data[inicio + 6]) + ((Util.unsignedByteToInt(data[inicio + 7]) & 0xF0) << 4));
                            processa52.mGrandezasC2.add((long) Util.unsignedByteToInt(data[inicio + 8]) + ((Util.unsignedByteToInt(data[inicio + 7]) & 0x0F) << 8));
                        }
                        inicio += 9;
                    }
                }
            }
            SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            StringBuilder dadosCSV = new StringBuilder();
            if (tipo_medidor != TipoMedidor.ABSOLUTO) {
                dadosCSV.append("Ponto;" + mDeviceName.substring(4) + "\n");
                dadosCSV.append("NS do medidor;" + respostaAbsoluto.getNumeroMedidor() + "\n");
            } else {
                dadosCSV.append("UC;" + medidorSelecionado.unidadeConsumidora + "\n");
                dadosCSV.append("NS do medidor;" + medidorSelecionado.numero + "\n");
            }

            dadosCSV.append("Data/Hora do ultimo periodo integrado;" + dadosAbnt51.textUltimoPeriodo + "\n");
            dadosCSV.append("Data/Hora da ultima fatura;" + dadosAbnt51.textUltimaFatura + "\n");
            dadosCSV.append("Numero de palavras;" + dadosAbnt51.numeroPalavras + "\n");
            dadosCSV.append("Intervalo da memoria de massa;" + dadosAbnt51.periodoIntegracao + "\n");
            dadosCSV.append("Constantes do Canal 1;" + dadosAbnt51.numeradorCanal1 + "/" + dadosAbnt51.denominadorCanal1 + "\n");
            dadosCSV.append("Constantes do Canal 2;" + dadosAbnt51.numeradorCanal2 + "/" + dadosAbnt51.denominadorCanal2 + "\n");
            dadosCSV.append("Constantes do Canal 3;" + dadosAbnt51.numeradorCanal3 + "/" + dadosAbnt51.denominadorCanal3 + "\n");


            String tmp = "Reg;Data/Hora;";
            String format = "";
            String strCanal = "";
            int fator_multiplicacao = 1;

            if (tipo_medidor == TipoMedidor.EASY_VOLT) {
                switch (codigoCanal) {
                    case 0:
                        format = ";%.02f;%d;%.02f;%d;%.02f;%d;\n";
                        tmp += "V_a;C1;V_b;C2;V_c;C3";
                        strCanal = "Tensao";
                        break;
                    case 1:
                        format = ";%.02f;%d;%.02f;%d;%.02f;%d;\n";
                        tmp += "THD_VA;C1;THD_VB;C2;THD_VC;C3";
                        strCanal = "THDTensao";
                        break;
                    case 2:
                        format = ";%.02f;%d;%.02f;%d;%.02f;%d;\n";
                        tmp += "VAminh;C1;VBminh;C2;VCminh;C3";
                        strCanal = "TensaoMinima";
                        break;
                    case 3:
                        format = ";%.02f;%d;%.02f;%d;%.02f;%d;\n";
                        tmp += "VAmaxh;C1;VBmaxh;C2;VCmaxh;C3";
                        strCanal = "TensaoMaxima";
                        break;
                }
            } else {
                switch (codigoCanal) {
                    case 0:
                        tmp += "W;C1;varIND;C2;varCAP;C3";
                        format = ";%.00f;%d;%.00f;%d;%.00f;%d;\n";
                        fator_multiplicacao = 1000;
                        strCanal = "EnergiaDireta";
                        break;
                    case 1:
                        tmp += "-W;C1;varIND;C2;varCAP;C3";
                        format = ";%.00f;%d;%.00f;%d;%.00f;%d;\n";
                        fator_multiplicacao = 1000;
                        strCanal = "EnergiaReversa";
                        break;
                    case 2:
                        format = ";%.02f;%d;%.02f;%d;%.02f;%d;\n";
                        tmp += "V_a;C1;V_b;C2;V_c;C3";
                        strCanal = "Tensao";
                        break;
                    case 3:
                        format = ";%.02f;%d;%.02f;%d;%.02f;%d;\n";
                        tmp += "I_a;C1;I_b;C2;I_c;C3";
                        strCanal = "Corrente";
                        break;
                    case 4:
                        format = ";%.02f;%d;%.02f;%d;%.02f;%d;\n";
                        tmp += "THD_VA;C1;THD_VB;C2;THD_VC;C3";
                        strCanal = "THDTensao";
                        break;
                    case 5:
                        format = ";%.02f;%d;%.02f;%d;%.02f;%d;\n";
                        tmp += "THD_IA;C1;THD_IB;C2;THD_IC;C3";
                        strCanal = "THDCorrente";
                        break;
                }
            }
            tmp += "\n";
            dadosCSV.append(tmp);
            for (int i = 0; i < dadosAbnt51.totalGrandezas; i++) {
                float v1 = processa52.mGrandezasC1.get(i) * ((float) dadosAbnt51.numeradorCanal1 / (float) dadosAbnt51.denominadorCanal1 * (float) (60 / dadosAbnt51.periodoIntegracao)) * fator_multiplicacao;
                float v2 = processa52.mGrandezasC2.get(i) * ((float) dadosAbnt51.numeradorCanal2 / (float) dadosAbnt51.denominadorCanal2 * (float) (60 / dadosAbnt51.periodoIntegracao)) * fator_multiplicacao;
                float v3 = processa52.mGrandezasC3.get(i) * ((float) dadosAbnt51.numeradorCanal3 / (float) dadosAbnt51.denominadorCanal3 * (float) (60 / dadosAbnt51.periodoIntegracao)) * fator_multiplicacao;
                String linha = (i + 1) + ";" +
                        formatDate.format(dadosAbnt51.ultimoPeriodo.getTime()) +
                        String.format(format,
                                v1, processa52.mGrandezasC1.get(i), v2, processa52.mGrandezasC2.get(i), v3, processa52.mGrandezasC3.get(i));
                dadosCSV.append(linha);
                dadosAbnt51.ultimoPeriodo.add(Calendar.MINUTE, dadosAbnt51.periodoIntegracao);
            }

            String nomeArquivo = String.format("MemoriaMassa%s_%s_%s_%s.csv", strCanal, respostaAbsoluto.getNumeroMedidor(), mDeviceName.substring(4).trim(), new SimpleDateFormat("ddMMyyyy'_'HHmmss").format(Calendar.getInstance().getTime()));

            if (tipo_medidor == TipoMedidor.ABSOLUTO) {
                nomeArquivo = String.format("MemoriaMassa%s_%s_%s_%s.csv", strCanal, medidorSelecionado.numero, medidorSelecionado.unidadeConsumidora /*mDeviceName.substring(4).trim()*/,
                        new SimpleDateFormat("ddMMyyyy'_'HHmmss").format(Calendar.getInstance().getTime()));
            }

            if (!Arquivo.salvarArquivo(this, nomeArquivo, dadosCSV.toString())) {
                Toast.makeText(this, "Erro ao salvar arquivo da memória de massa",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Arquivo salvo com sucesso:\n" + nomeArquivo,
                        Toast.LENGTH_LONG).show();
            }

            progressDialog.hide();
            progressDialog.dismiss();
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    private void processarRespostaAbnt73(RespostaAbsoluto resposta) {
        if (leuEasyTrafo)
            return;

        mTimeoutHandler.removeCallbacksAndMessages(null);
        progressDialog.dismiss();
        mHandler.removeCallbacksAndMessages(null);
        leuEasyTrafo = true;
        //Log.d(TAG, "processarRespostaAbnt73.");
        byte[] data = resposta.getData();

        //numMedidor = new String(Arrays.copyOfRange(data, 7, 21));

        mNsMedidor = resposta.getNumeroMedidor();

        mMeterEasyTrafoAdapter = new MeterEasyTrafoAdapter(numMedidor, dataMedidor, mNsMedidor);
        mRecyclerView.setAdapter(mMeterEasyTrafoAdapter);
        mMeterEasyTrafoAdapter.notifyDataSetChanged();

        mRefreshStatus.clearAnimation();

    }

    private void processarRespostaAbnt87(RespostaAbsoluto resposta) {
        if (leuEasyTrafo)
            return;

        mTimeoutHandler.removeCallbacksAndMessages(null);
        progressDialog.dismiss();
        mHandler.removeCallbacksAndMessages(null);
        leuEasyTrafo = true;
        Log.d(TAG, "processarRespostaAbnt87.");
        byte[] data = resposta.getData();
        numMedidor = new String(Arrays.copyOfRange(data, 7, 21));

        mNsMedidor = resposta.getNumeroMedidor();

        mMeterEasyTrafoAdapter = new MeterEasyTrafoAdapter(numMedidor, dataMedidor, mNsMedidor);
        mRecyclerView.setAdapter(mMeterEasyTrafoAdapter);
        mMeterEasyTrafoAdapter.notifyDataSetChanged();

        mRefreshStatus.clearAnimation();

    }

    private void processarRespostaAbnt21Absoluto(RespostaAbsoluto resposta) {
        byte[] data = resposta.getData();
        dataMedidor = String.format("%02X/%02X/20%02X %02X:%02X:%02X",
                data[8], data[9], data[10], data[5], data[6], data[7]);

        if (tipo_medidor == TipoMedidor.ABSOLUTO) {
            versaoMedidor = String.format("%02X.%02X", data[30], data[31]);
        } else {
            versaoMedidor = String.format("%02X.%02X", data[147], data[148]);
        }
        modeloMultiponto = String.format("%02X%02X", data[152], data[153]);

        tipoModulo = String.format("%02X", data[37]);
        estadoModulo = String.format("%02X", data[38]);
        sinalModulo = String.valueOf(data[39]);
        MetodoNumeroSerial = data[40];
        switch (tipoModulo) {
            case "01":
                tipoModulo = "Star Measure";
                break;
            case "02":
                tipoModulo = "CAS";
                break;
            case "03":
                tipoModulo = "M2M";
                break;
            case "04":
                tipoModulo = "V2COM";
                break;
            case "05":
                tipoModulo = "SSE Gridtech";
                break;
            case "06":
                tipoModulo = "Honeywell";
                break;
            case "07":
                tipoModulo = "Landys+Gyr";
                break;
            case "08":
                tipoModulo = "Itron";
                break;
            default:
                tipoModulo = "";
                break;
        }
        //estadoModulo = String.format("%02X", data[38]);
        if (estadoModulo.equals("01")) {
            estadoModulo = "Conectado";
        } else {
            estadoModulo = "Desconectado";
        }
        //sinalModulo = String.format("%02X", data[40]);

        Log.d(TAG, "\nProcessa ABNT 21 Absoluto { \nData: " + dataMedidor + "\nVersão Medidor: " + versaoMedidor + "\nModelo multiponto: " + modeloMultiponto + "}");

        enviarLeituraParametrosHospedeiro();
        //enviarLeituraConfiguracao(tipo_medidor);
    }

    private void processarRespostaAbnt21(RespostaAbsoluto resposta) {
        if (leuEasyTrafo)
            return;

        byte[] data = resposta.getData();
        //date = Calendar.getInstance().getTime();
        dataMedidor = String.format("%02X/%02X/20%02X %02X:%02X:%02X",
                data[8], data[9], data[10], data[5], data[6], data[7]);

        versaoMedidor = String.format("%02X.%02X", data[147], data[148]);
        modeloMultiponto = String.format("%02X%02X", data[152], data[153]);

        if (modeloMultiponto.equals("1503")) {
            tipo_medidor = TipoMedidor.EASY_VOLT;
        }
        else if(modeloMultiponto.equals("1603")){
            easytrafol = true;
            tipo_medidor = TipoMedidor.EASY_TRAFO;
        }

        tipoModulo = String.format("%02X", data[37]);
        switch (tipoModulo) {
            case "00":
                tipoModulo = "";
                break;
            case "01":
                tipoModulo = "Star Measure";
                break;
            case "02":
                tipoModulo = "CAS";
                break;
            case "03":
                tipoModulo = "M2M";
                break;
            case "04":
                tipoModulo = "V2COM";
                break;
            case "05":
                tipoModulo = "SSE Gridtech";
                break;
            case "06":
                tipoModulo = "Honeywell";
                break;
            case "07":
                tipoModulo = "Landys+Gyr";
                break;
            case "08":
                tipoModulo = "Itron";
                break;
            default:
                tipoModulo = "";
                break;
        }
        estadoModulo = String.format("%02X", data[38]);
        if (estadoModulo.equals("01")) {
            estadoModulo = "Conectado";
        } else {
            estadoModulo = "Desconectado";
        }
        sinalModulo = String.valueOf(data[39]);
        versaoFWModCom = String.format("%02X", data[41]);
        revisaoFWModCom = String.format("%02X", data[42]);
        versaoModCom = versaoFWModCom + "." + revisaoFWModCom;
        tipoModComAux = data[43];
        if(tipoModComAux == 0){
            tipoModCom = "UG96";
        }
        else if(tipoModComAux == 1){
            tipoModCom = "UG89";
        }

        Log.d(TAG, "\nProcessa ABNT 21 Easy { \nData: " + dataMedidor + "\nVersão Medidor: " + versaoMedidor + "\nModelo multiponto: " + modeloMultiponto + "}");
        enviarLeituraConfiguracao(tipo_medidor);
    }

    private void atualizarStatusMedidor(MedidorAbsoluto
                                                medidor, RespostaAbsoluto.LeituraStatusMedidor status) {
        medidor.status = status.EstadoUnidadeConsumidora;
        medidor.semRele = false;
        mMeterListAdapter.atualizarDados(medidores);
        mMeterListAdapter.notifyDataSetChanged();
    }

    private void processarRespostaCorteReligamento(RespostaAbsoluto respostaAbsoluto) {
        String numeroMedidor = "";
        if((MetodoNumeroSerial == 0x00)||(MetodoNumeroSerial == 0x01)){
            numeroMedidor = respostaAbsoluto.getSufixoNumeroMedidor();
        }
        else{
            numeroMedidor = respostaAbsoluto.getNumeroMedidor();
        }
        enviarLeituraStatusMedidores(numeroMedidor, MetodoNumeroSerial);
    }

    private void processarRespostaAberturaSessao(byte codigoResposta, RespostaAbsoluto
            respostaAbsoluto) {
        String numeroMedidor = "";
        if(MetodoNumeroSerial == 0x02){
            numeroMedidor = respostaAbsoluto.getNumeroMedidor();
        }
        else{
            numeroMedidor = respostaAbsoluto.getSufixoNumeroMedidor();
        }
        Log.d(TAG, "Abertura de sessão para o medidor: " + numeroMedidor + "\n" + respostaAbsoluto.toString());

        if (codigoResposta == 0x01) {
            Toast.makeText(DeviceActivity.this, getString(R.string.text_senha_expira),
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

        for (MedidorAbsoluto medidor : medidores) {
            //medidor.
            if ((medidor.numero.endsWith(numeroMedidor)) || (medidor.numero.startsWith("99" + numeroMedidor))) {
                if (TipoOperacao.CorteReliga == funcaoEmExecucao) {
                    if (medidor.status == 0x00) {
                        enviarReligamento(numeroMedidor, MetodoNumeroSerial);
                    } else if (medidor.status == 0x01) {
                        enviarCorte(numeroMedidor, MetodoNumeroSerial);
                    }
                } else {
                    ajustaDataHora(true, medidor);
                }
            }
        }
    }

    private void showErrorMessage(String mensagem) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(DeviceActivity.this,
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

    private void processarRespostaNaoTratada(byte[] data) {
        Log.d(TAG, "Resposta não tratada: " + Util.ByteArrayToHexString(data));
    }

    private void enviarProximoStatusMedidor(String numeroMedidor) {
        if (numeroMedidor.equals("99999999") && medidores.size() > 1) {
            deveLerTudo = true;
            String numero = medidores.get(0).numero;
            if (numero.endsWith("000000"))
                numero = numero.substring(0, numero.length() - 6);
            enviarLeituraStatusMedidores(numero, MetodoNumeroSerial);
            return;
        }
        for (int i = 0; i < medidores.size(); i++) {
            if (medidores.get(i).numero.endsWith(numeroMedidor)) {
                if (i != medidores.size() - 1) {
                    String numero = medidores.get(i + 1).numero;
                    enviarLeituraStatusMedidores(numero, MetodoNumeroSerial);
                    return;
                } else {
                    Log.d(TAG, "RemoveCallbacks status medidores");
                    mTimeoutHandler.removeCallbacksAndMessages(null);
                    mRefreshStatus.clearAnimation();
                    deveLerTudo = false;
                    return;
                }
            }
        }
        if (medidores.size() > 1)
            enviarLeituraStatusMedidores(medidores.get(1).numero, MetodoNumeroSerial);
    }

    private void enviarCorte(String numeroMedidor, int metodo) {
        byte[] comando = new ComandoAbsoluto.AB06AlteracaoCorteReligamento()
                .comMedidorNumero(numeroMedidor, metodo)
                .efetuaCorte()
                .build((tipo_medidor == TipoMedidor.ABSOLUTO), MetodoNumeroSerial);
        enviarComando(comando);
    }

    private void enviarReligamento(String numeroMedidor, int metodo) {
        byte[] comando = new ComandoAbsoluto.AB06AlteracaoCorteReligamento()
                .comMedidorNumero(numeroMedidor, metodo)
                .efetuaReligamento()
                .build((tipo_medidor == TipoMedidor.ABSOLUTO), MetodoNumeroSerial);
        enviarComando(comando);
    }

    private void enviarTipoLigacao(String numeroMedidor, int metodo) {
        byte[] comando = new ComandoAbsoluto.ABNT95()
                .comMedidorNumero(numeroMedidor, metodo)
                .build((byte) codigoCanal, metodo);
        enviarComando(comando);
    }

    private void enviarLimparOcorrencia(String numeroMedidor, byte codigoOcorrencia, int metodo) {
        final byte[] comando = new ComandoAbsoluto.LimpezaOcorrenciasMedidor()
                .comMedidorNumero(numeroMedidor, metodo)
                .comCodigoOcorrencia(codigoOcorrencia)
                .build((tipo_medidor == TipoMedidor.ABSOLUTO), metodo);
        enviarComando(comando);
    }

    private void enviarComandoComposto(byte[] data) {
        mRespostaComposta.clear();
        Log.d(TAG, "ENC->" + Util.ByteArrayToHexString(data));
        //Log.d(TAG, Util.ByteArrayToString(data));
        if (!sendData(data))
            Log.d(TAG, "Erro ao enviar mensagem");

    }

    private void enviarComando(byte[] data) {
        Log.d(TAG, "ENS->" + Util.ByteArrayToHexString(data));
        if (!sendData(data))
            Log.d(TAG, "Erro ao enviar mensagem");
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    private class MeterListAdapter
            extends RecyclerView.Adapter<MeterListAdapter.MeterViewHolder> {

        private List<MedidorAbsoluto> medidores;
        private String data_hora = "";

        public void atualizarDados(List<MedidorAbsoluto> medidores) {
            this.medidores = medidores;
        }

        public class MeterViewHolder extends RecyclerView.ViewHolder {
            private final OnClickListener incidenteClick = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() >= 0) {
                        medidorSelecionado = medidores.get(getAdapterPosition());
                        exibirMenu(v);
                    }
                }
            };

            TextView unidadeConsumidoraInfo;
            TextView unidadeConsumidora;
            TextView medidorInfo;
            TextView medidor;
            TextView dataHora;
            TextView dataHoraInfo;
            TextView versao;
            TextView versaoInfo;
            TextView modComTitulo;
            TextView modComInfo;
            TextView fases;
            //TextView versaoInfo;
            //TextView versao;
            //TextView fases;
            TextView moduloNomeTitulo;
            TextView moduloNome;
            TextView tipoModuloTitulo;
            TextView tipoModuloInfo;
            TextView moduloEstadoTitulo;
            TextView moduloEstado;
            TextView moduloSinalTitulo;
            TextView moduloSinal;
            ImageView statusMedidor;
            //ImageView statusMedidor;

            public MeterViewHolder(@NonNull View view) {
                super(view);
                unidadeConsumidora = view.findViewById(R.id.unidade_consumidora);
                unidadeConsumidoraInfo = view.findViewById(R.id.unidade_consumidora_info);
                medidor = view.findViewById(R.id.dados_medidor);
                medidorInfo = view.findViewById(R.id.dados_medidor_info);
                dataHora = view.findViewById(R.id.dados_medidor);
                dataHoraInfo = view.findViewById(R.id.dados_medidor_info);
                versao = view.findViewById(R.id.medidor_versao);
                versaoInfo = view.findViewById(R.id.medidor_versao_info);
                modComTitulo = view.findViewById(R.id.modCom_versao_info_title);
                modComInfo = view.findViewById(R.id.mi_tv_modCom_versao);
                fases = view.findViewById(R.id.numero_fases_medidor);
                //fases.setVisibility(LinearLayout.GONE);
                statusMedidor = view.findViewById(R.id.status_medidor);
                //statusMedidor.setVisibility(LinearLayout.GONE);
                unidadeConsumidora.setOnClickListener(incidenteClick);
                view.findViewById(R.id.incidente_medidor).setOnClickListener(incidenteClick);
                unidadeConsumidora.setOnClickListener(incidenteClick);
                moduloNome = view.findViewById(R.id.mi_tv_modulo_nome);
                moduloNomeTitulo = view.findViewById(R.id.mi_tv_modulo_nome_title);
                tipoModuloInfo = view.findViewById(R.id.mi_tv_tipo_modcom);
                tipoModuloTitulo = view.findViewById(R.id.mi_tv_tipo_nome_title);
                moduloEstado = view.findViewById(R.id.mi_tv_modulo_estado);
                moduloEstadoTitulo = view.findViewById(R.id.mi_tv_modulo_estado_title);
                moduloSinal = view.findViewById(R.id.mi_tv_modulo_sinal);
                moduloSinalTitulo = view.findViewById(R.id.mi_tv_modulo_sinal_title);

                //versao = view.findViewById(R.id.medidor_versao);
                //versaoInfo = view.findViewById(R.id.medidor_versao_info);
                //fases = view.findViewById(R.id.numero_fases_medidor);
                //unidadeConsumidora.setOnClickListener(incidenteClick);
                //statusMedidor = view.findViewById(R.id.status_medidor);
                view.findViewById(R.id.incidente_medidor).setOnClickListener(incidenteClick);
            }
        }

        public MeterListAdapter(List<MedidorAbsoluto> medidores) {
            this.medidores = medidores;
        }

        public void clear() {
            medidores.clear();
        }

        @NonNull
        @Override
        public MeterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.meter_item, viewGroup, false);
            return new MeterViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MeterViewHolder meterViewHolder, int i) {
            int PosicaoReal = meterViewHolder.getAdapterPosition();
            MedidorAbsoluto medidor = medidores.get(i);
            if(i == 5){
                PosicaoReal = 5;
            }
            String formato = "%s";
            String str_medidor = "";
            //if(MetodoNumeroSerial == 2){
            //    str_medidor = medidor.numero;
            //}
            //else{
                str_medidor = medidor.unidadeConsumidora;
            //}

            String str_fases = String.format("%d", medidor.fases);
            int status = medidor.status;

            if(MetodoNumeroSerial == 2){
                if (str_medidor.equalsIgnoreCase(medidores.get(0).unidadeConsumidora)) {
                    //meterViewHolder.unidadeConsumidoraInfo.setVisibility(LinearLayout.GONE);
                    meterViewHolder.unidadeConsumidoraInfo.setText("Hospedeiro:");
                    Log.d("Recycler", "Medidor : " + medidor.numero + " - Posicao na tela:" + Integer.valueOf(meterViewHolder.getAdapterPosition()).toString());
                    //meterViewHolder.unidadeConsumidora.setTextColor(getResources().getColor(R.color.starmeasure_turquoise));
                    meterViewHolder.dataHoraInfo.setText("Data e Hora:");
                    meterViewHolder.dataHora.setText(dataMedidor);
                    meterViewHolder.versaoInfo.setVisibility(LinearLayout.VISIBLE);
                    meterViewHolder.versao.setVisibility(LinearLayout.VISIBLE);
                    meterViewHolder.versao.setText(versaoMedidor);
                    if (!tipoModulo.isEmpty()) {
                        if (tipo_medidor == TipoMedidor.EASY_VOLT) {
                            meterViewHolder.modComTitulo.setVisibility(LinearLayout.VISIBLE);
                            meterViewHolder.modComInfo.setVisibility(LinearLayout.VISIBLE);
                            meterViewHolder.tipoModuloTitulo.setVisibility(LinearLayout.VISIBLE);
                            meterViewHolder.tipoModuloInfo.setVisibility(LinearLayout.VISIBLE);

                            meterViewHolder.modComInfo.setText(versaoModCom);
                            meterViewHolder.tipoModuloInfo.setText(tipoModCom);
                        }else{
                            meterViewHolder.modComTitulo.setVisibility(LinearLayout.GONE);
                            meterViewHolder.modComInfo.setVisibility(LinearLayout.GONE);
                            meterViewHolder.tipoModuloTitulo.setVisibility(LinearLayout.GONE);
                            meterViewHolder.tipoModuloInfo.setVisibility(LinearLayout.GONE);
                        }
                        meterViewHolder.moduloNomeTitulo.setVisibility(LinearLayout.VISIBLE);
                        meterViewHolder.moduloNome.setVisibility(LinearLayout.VISIBLE);
                        meterViewHolder.moduloEstado.setVisibility(LinearLayout.VISIBLE);
                        meterViewHolder.moduloEstadoTitulo.setVisibility(LinearLayout.VISIBLE);
                        meterViewHolder.moduloSinal.setVisibility(LinearLayout.VISIBLE);
                        meterViewHolder.moduloSinalTitulo.setVisibility(LinearLayout.VISIBLE);

                        meterViewHolder.moduloNome.setText(tipoModulo);
                        meterViewHolder.moduloEstado.setText(estadoModulo);
                        meterViewHolder.moduloSinal.setText(sinalModulo+" dBm");
                    }


                    SimpleDateFormat smf = new SimpleDateFormat("dd/MM/yyyy' 'HH:mm:ss");
                    Date dt = new Date();
                    try {
                        dt = smf.parse(dataMedidor);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    long diff = System.currentTimeMillis() - dt.getTime();
                    long seconds = diff / 1000;

                    if (seconds < 0) {
                        seconds *= -1;
                    }

                    if (seconds > 300) {
                        meterViewHolder.dataHora.setTextColor(Color.RED);
                    }

                    //str_medidor = "Medidor Hospedeiro " + medidor.numero.substring(0, 8);
                    //str_medidor = medidor.numero.substring(0, 8);

                    //str_medidor = medidor.unidadeConsumidora;
                    str_medidor = medidor.numero.substring(0, 8);
                    str_fases = "";
                    status = -1;
                } else {
                    meterViewHolder.unidadeConsumidoraInfo.setText("UC:");
                    meterViewHolder.medidorInfo.setText("Medidor:");
                    meterViewHolder.medidor.setTextColor(getColor(R.color.textDark));
                    meterViewHolder.versao.setVisibility(LinearLayout.GONE);
                    meterViewHolder.versaoInfo.setVisibility(LinearLayout.GONE);
                    meterViewHolder.modComTitulo.setVisibility(LinearLayout.GONE);
                    meterViewHolder.modComInfo.setVisibility(LinearLayout.GONE);
                    meterViewHolder.medidor.setText(medidor.numero);
                    meterViewHolder.tipoModuloTitulo.setVisibility(LinearLayout.GONE);
                    meterViewHolder.tipoModuloInfo.setVisibility(LinearLayout.GONE);
                }

                meterViewHolder.unidadeConsumidora.setText(String.format(formato, str_medidor));


                switch (status) {
                    case 0x00:
                        meterViewHolder.statusMedidor.setImageResource(R.drawable.power_off);
                        break;
                    case 0x01:
                        meterViewHolder.statusMedidor.setImageResource(R.drawable.status_ok);
                        break;
                    case -1:
                        meterViewHolder.statusMedidor.setImageResource(android.R.color.transparent);
                        break;
                    default:
                        meterViewHolder.statusMedidor.setImageResource(R.drawable.status_warning);
                }
                meterViewHolder.fases.setText(str_fases);
            }
            else{
                if (str_medidor.equalsIgnoreCase("000000000")) {
                    //meterViewHolder.unidadeConsumidoraInfo.setVisibility(LinearLayout.GONE);
                    meterViewHolder.unidadeConsumidoraInfo.setText("Hospedeiro:");
                    Log.d("Recycler", "Medidor : " + medidor.numero + " - Posicao na tela:" + Integer.valueOf(meterViewHolder.getAdapterPosition()).toString());
                    //meterViewHolder.unidadeConsumidora.setTextColor(getResources().getColor(R.color.starmeasure_turquoise));
                    meterViewHolder.dataHoraInfo.setText("Data e Hora:");
                    meterViewHolder.dataHora.setText(dataMedidor);
                    meterViewHolder.versaoInfo.setVisibility(LinearLayout.VISIBLE);
                    meterViewHolder.versao.setVisibility(LinearLayout.VISIBLE);
                    meterViewHolder.versao.setText(versaoMedidor);
                    if (!tipoModulo.isEmpty()) {
                        if (tipo_medidor == TipoMedidor.EASY_VOLT) {
                            meterViewHolder.modComTitulo.setVisibility(LinearLayout.VISIBLE);
                            meterViewHolder.modComInfo.setVisibility(LinearLayout.VISIBLE);
                            meterViewHolder.tipoModuloTitulo.setVisibility(LinearLayout.VISIBLE);
                            meterViewHolder.tipoModuloInfo.setVisibility(LinearLayout.VISIBLE);

                            meterViewHolder.modComInfo.setText(versaoModCom);
                            meterViewHolder.tipoModuloInfo.setText(tipoModCom);
                        }else{
                            meterViewHolder.modComTitulo.setVisibility(LinearLayout.GONE);
                            meterViewHolder.modComInfo.setVisibility(LinearLayout.GONE);
                            meterViewHolder.tipoModuloTitulo.setVisibility(LinearLayout.GONE);
                            meterViewHolder.tipoModuloInfo.setVisibility(LinearLayout.GONE);
                        }
                        meterViewHolder.moduloNomeTitulo.setVisibility(LinearLayout.VISIBLE);
                        meterViewHolder.moduloNome.setVisibility(LinearLayout.VISIBLE);
                        meterViewHolder.moduloEstado.setVisibility(LinearLayout.VISIBLE);
                        meterViewHolder.moduloEstadoTitulo.setVisibility(LinearLayout.VISIBLE);
                        meterViewHolder.moduloSinal.setVisibility(LinearLayout.VISIBLE);
                        meterViewHolder.moduloSinalTitulo.setVisibility(LinearLayout.VISIBLE);

                        meterViewHolder.moduloNome.setText(tipoModulo);
                        meterViewHolder.moduloEstado.setText(estadoModulo);
                        meterViewHolder.moduloSinal.setText(sinalModulo+" dBm");
                    }


                    SimpleDateFormat smf = new SimpleDateFormat("dd/MM/yyyy' 'HH:mm:ss");
                    Date dt = new Date();
                    try {
                        dt = smf.parse(dataMedidor);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    long diff = System.currentTimeMillis() - dt.getTime();
                    long seconds = diff / 1000;

                    if (seconds < 0) {
                        seconds *= -1;
                    }

                    if (seconds > 300) {
                        meterViewHolder.dataHora.setTextColor(Color.RED);
                    }

                    //str_medidor = "Medidor Hospedeiro " + medidor.numero.substring(0, 8);
                    //str_medidor = medidor.numero.substring(0, 8);
                    str_medidor = medidor.unidadeConsumidora;
                    str_fases = "";
                    status = -1;
                } else {
                    meterViewHolder.unidadeConsumidoraInfo.setText("UC:");
                    meterViewHolder.medidorInfo.setText("Medidor:");
                    meterViewHolder.medidor.setTextColor(getColor(R.color.textDark));
                    meterViewHolder.versao.setVisibility(LinearLayout.GONE);
                    meterViewHolder.versaoInfo.setVisibility(LinearLayout.GONE);
                    meterViewHolder.modComTitulo.setVisibility(LinearLayout.GONE);
                    meterViewHolder.modComInfo.setVisibility(LinearLayout.GONE);
                    meterViewHolder.medidor.setText(medidor.numero);
                    meterViewHolder.tipoModuloTitulo.setVisibility(LinearLayout.GONE);
                    meterViewHolder.tipoModuloInfo.setVisibility(LinearLayout.GONE);
                }

                meterViewHolder.unidadeConsumidora.setText(String.format(formato, str_medidor));


                switch (status) {
                    case 0x00:
                        meterViewHolder.statusMedidor.setImageResource(R.drawable.power_off);
                        break;
                    case 0x01:
                        meterViewHolder.statusMedidor.setImageResource(R.drawable.status_ok);
                        break;
                    case -1:
                        meterViewHolder.statusMedidor.setImageResource(android.R.color.transparent);
                        break;
                    default:
                        meterViewHolder.statusMedidor.setImageResource(R.drawable.status_warning);
                }
                meterViewHolder.fases.setText(str_fases);
            }
        }

        @Override
        public int getItemCount() {
            return medidores.size();
        }
    }

    private void exibirMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.menu_acoes);
        MenuItem item_corte_religa = popup.getMenu().findItem(R.id.menu_corte_religa);
        MenuItem item_qee = popup.getMenu().findItem(R.id.menu_qee);
        MenuItem item_monitoramento = popup.getMenu().findItem(R.id.menu_monitoramento);
        MenuItem item_configuracao_nic = popup.getMenu().findItem(R.id.menu_configuracao_nic);
        MenuItem item_registradores = popup.getMenu().findItem(R.id.menu_leitura_registradores);
        MenuItem item_reset_registradores = popup.getMenu().findItem(R.id.menu_reset_registradores);
        MenuItem item_modo_teste = popup.getMenu().findItem(R.id.menu_modo_test);
        MenuItem item_calibracao = popup.getMenu().findItem(R.id.menu_calibracao);
        item_qee.setVisible(false);

        if (tipo_medidor == TipoMedidor.ABSOLUTO) {
            if (item_corte_religa != null) {
                if (medidorSelecionado.status == 0x00)
                    item_corte_religa.setTitle(getString(R.string.text_religa));
                else if (medidorSelecionado.status == 0x01)
                    item_corte_religa.setTitle(getString(R.string.text_corte));
                else
                    item_corte_religa.setTitle(getString(R.string.text_corte_religa));
            }
            if (medidorSelecionado.semRele) {
                if (item_corte_religa != null) {
                    item_corte_religa.setVisible(false);
                }
            }
            item_corte_religa = popup.getMenu().findItem(R.id.menu_nome_unidade);
            item_corte_religa.setVisible(false);
            item_corte_religa = popup.getMenu().findItem(R.id.menu_reset_registradores);
            item_corte_religa.setVisible(false);
        } else if (tipo_medidor == TipoMedidor.EASY_TRAFO) {
            item_corte_religa.setVisible(false);
            item_qee.setVisible(true);
            item_monitoramento.setVisible(true);
            if(easytrafol){
                if((iItem & 0x80) == 0x80){
                    item_calibracao.setVisible(true);
                }
            }
        } else if (tipo_medidor == TipoMedidor.EASY_VOLT) {

            item_reset_registradores.setVisible(false);
            item_registradores.setVisible(false);
            item_corte_religa.setVisible(false);
            item_qee.setVisible(true);
            item_monitoramento.setVisible(true);
            item_modo_teste.setVisible(true);
            if(tipoModulo == "M2M"){
                item_configuracao_nic.setVisible(true);
            }
        }
        else {
            item_corte_religa.setVisible(false);
        }
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_leitura_registradores:
                funcaoEmExecucao = TipoOperacao.RegistradoresAtuais;
                exibirLeituraParametros(medidorSelecionado);
                break;
            case R.id.menu_pagina_fiscal:
                funcaoEmExecucao = TipoOperacao.PaginaFiscal;
                exibirGrandezasInstantaneas(medidorSelecionado);
                break;
            case R.id.menu_corte_religa:
                funcaoEmExecucao = TipoOperacao.CorteReliga;
                enviarAberturaSessao(medidorSelecionado.numero, MetodoNumeroSerial);
                break;
            case R.id.menu_data_hora:
                funcaoEmExecucao = TipoOperacao.DataHora;
                if (tipo_medidor == TipoMedidor.ABSOLUTO) {
                    enviarAberturaSessao(medidorSelecionado.numero, MetodoNumeroSerial);
                } else {
                    ajustaDataHora(true, medidorSelecionado);
                }
                break;
            case R.id.menu_nome_unidade:
                funcaoEmExecucao = TipoOperacao.NomeUnidade;
                trocaNomeUnidade(medidorSelecionado);
                break;
            case R.id.menu_memoria_massa_abnt:
                funcaoEmExecucao = TipoOperacao.MemoriaMassa;
                iniciaMemoriaMassa(medidorSelecionado);
                break;
            case R.id.menu_memoria_massa_sm:
                funcaoEmExecucao = TipoOperacao.InicioMemoriaMassaSM;
                iniciaMemoriaMassaSM();
                break;
            case R.id.menu_memoria_massa_intervalo:
                funcaoEmExecucao = TipoOperacao.IntervaloMM;
                iniciaAlteracaoIntervaloMM(medidorSelecionado);
                break;
            case R.id.menu_memoria_massa_reset:
                funcaoEmExecucao = TipoOperacao.ResetRegistradores;
                resetRegistradores(medidorSelecionado, (byte) 2);
                break;
            case R.id.menu_reset_registradores:
                funcaoEmExecucao = TipoOperacao.ResetRegistradores;
                resetRegistradores(medidorSelecionado, (byte) 0);
                break;
            case R.id.menu_qee_parametrizacao:
                funcaoEmExecucao = TipoOperacao.ParametrosQEE;
                buscaQEEParametros();
                break;
            case R.id.menu_qee_reset:
                funcaoEmExecucao = TipoOperacao.ResetRegistradores;
                resetRegistradores(medidorSelecionado, (byte) 3);
                break;
            case R.id.menu_qee_leitura:
                funcaoEmExecucao = TipoOperacao.InicioMemoriaMassaQEE;
                iniciaMemoriaMassaQEE();
                break;
            case R.id.menu_carga_de_programa:
                funcaoEmExecucao = TipoOperacao.IniciarCargaDePrograma;
                lancaCargaDePrograma();
                break;
            case R.id.menu_configuracao:
                funcaoEmExecucao = TipoOperacao.IniciarConfiguracao;
                iniciaConfiguracao();
                break;
            case R.id.menu_modo_prateleira:
                funcaoEmExecucao = TipoOperacao.IniciaModoPrateleira;
                Prateleira();
                break;
            case R.id.menu_NIC_manual:
                funcaoEmExecucao = TipoOperacao.ConfiguracaoNIC;
                iniciaConfiguracaoNIC();
                break;
            case R.id.menu_NIC_predefinicao:
                funcaoEmExecucao = TipoOperacao.PredefinicaoNIC;
                iniciaPredefinicaoNIC();
                break;
            case R.id.menu_monitoramento:
                funcaoEmExecucao = TipoOperacao.MonitoramentoDeTransformador;
                iniciaMonitoramento();
                break;
            case R.id.menu_calibracao:
                funcaoEmExecucao = TipoOperacao.MenuCalibracao;
                iniciaCalibracao();
                break;
            case R.id.menu_modo_test:
                funcaoEmExecucao = TipoOperacao.ModoTeste;
                eb92 = new ComandoAbsoluto.EB92();
                iniciaModoTeste();
                break;
            default:
                return false;
        }
        return true;
    }

    private void iniciaModoTeste() {
        enviarComando(eb92.build());
        showProgressBar("Buscando alarmes...");
    }

    private void processaEB92(RespostaAbsoluto resposta) {
        RespostaAbsoluto.Alarmes alarmes = resposta.processaAlarmes();
        eb92.alarmeEstadoTampa = alarmes.estadoTampa;
        eb92.alarmeSubTensaoA = alarmes.subtensaoA;
        eb92.alarmeSubTensaoB = alarmes.subtensaoB;
        eb92.alarmeSubTensaoC = alarmes.subtensaoC;
        eb92.alarmeSobreTensaoA = alarmes.sobretensaoA;
        eb92.alarmeSobreTensaoB = alarmes.sobretensaoB;
        eb92.alarmeSobreTensaoC = alarmes.sobretensaoC;

        CheckBox cbAlarmeSobretensaoA;
        CheckBox cbAlarmeSobretensaoB;
        CheckBox cbAlarmeSobretensaoC;
        CheckBox cbAlarmeSubtensaoA;
        CheckBox cbAlarmeSubtensaoB;
        CheckBox cbAlarmeSubtensaoC;
        CheckBox cbEstadoTampa;

        if (dialogModoTeste == null) {
            mMMRunnable = null;
            mHandler.removeCallbacksAndMessages(null);
            progressDialog.dismiss();
            dialogModoTeste = new Dialog(this);
            dialogModoTeste.setContentView(R.layout.dialog_modo_teste);
            if (dialogModoTeste.getWindow() != null) {
                dialogModoTeste.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            dialogModoTeste.setCancelable(false);
            dialogModoTeste.setOnDismissListener(dialog -> dialogModoTeste = null);

            dialogModoTeste.findViewById(R.id.modo_teste_btn_close).setOnClickListener(view -> {
                dialogModoTeste.dismiss();
                eb92 = null;
                dialogModoTeste = null;
            });

            dialogModoTeste.findViewById(R.id.modo_teste_enviar).setOnClickListener(v -> {
                Toast.makeText(this, "Enviando alteração de teste", Toast.LENGTH_SHORT).show();
                dialogModoTeste.findViewById(R.id.modo_teste_loading).setVisibility(VISIBLE);
                eb92.isEscrita = true;
                enviarComando(eb92.build());
                v.setEnabled(false);
            });

            cbAlarmeSobretensaoA = dialogModoTeste.findViewById(R.id.cb_alarme_sobretensao_a);
            cbAlarmeSobretensaoA.setChecked(alarmes.sobretensaoA);
            cbAlarmeSobretensaoA.setOnCheckedChangeListener((buttonView, isChecked) -> eb92.alarmeSobreTensaoA = isChecked);

            cbAlarmeSobretensaoB = dialogModoTeste.findViewById(R.id.cb_alarme_sobretensao_b);
            cbAlarmeSobretensaoB.setChecked(alarmes.sobretensaoB);
            cbAlarmeSobretensaoB.setOnCheckedChangeListener((buttonView, isChecked) -> eb92.alarmeSobreTensaoB = isChecked);

            cbAlarmeSobretensaoC = dialogModoTeste.findViewById(R.id.cb_alarme_sobretensao_c);
            cbAlarmeSobretensaoC.setChecked(alarmes.sobretensaoC);
            cbAlarmeSobretensaoC.setOnCheckedChangeListener((buttonView, isChecked) -> eb92.alarmeSobreTensaoC = isChecked);

            cbAlarmeSubtensaoA = dialogModoTeste.findViewById(R.id.cb_alarme_subtensao_a);
            cbAlarmeSubtensaoA.setChecked(alarmes.subtensaoA);
            cbAlarmeSubtensaoA.setOnCheckedChangeListener((buttonView, isChecked) -> eb92.alarmeSubTensaoA = isChecked);

            cbAlarmeSubtensaoB = dialogModoTeste.findViewById(R.id.cb_alarme_subtensao_b);
            cbAlarmeSubtensaoB.setChecked(alarmes.subtensaoB);
            cbAlarmeSubtensaoB.setOnCheckedChangeListener((buttonView, isChecked) -> eb92.alarmeSubTensaoB = isChecked);

            cbAlarmeSubtensaoC = dialogModoTeste.findViewById(R.id.cb_alarme_subtensao_c);
            cbAlarmeSubtensaoC.setChecked(alarmes.subtensaoC);
            cbAlarmeSubtensaoC.setOnCheckedChangeListener((buttonView, isChecked) -> eb92.alarmeSubTensaoC = isChecked);

            cbEstadoTampa = dialogModoTeste.findViewById(R.id.cb_alarme_estado_tampa);
            cbEstadoTampa.setChecked(alarmes.estadoTampa);
            cbEstadoTampa.setOnCheckedChangeListener((buttonView, isChecked) -> eb92.alarmeEstadoTampa = isChecked);
            dialogModoTeste.show();
        } else {
            Toast.makeText(this, "Dados recebidos...", Toast.LENGTH_SHORT).show();
            dialogModoTeste.findViewById(R.id.modo_teste_loading).setVisibility(GONE);
            dialogModoTeste.findViewById(R.id.modo_teste_enviar).setEnabled(true);

            cbAlarmeSobretensaoA = dialogModoTeste.findViewById(R.id.cb_alarme_sobretensao_a);
            cbAlarmeSobretensaoA.setChecked(alarmes.sobretensaoA);
            cbAlarmeSobretensaoA.setOnCheckedChangeListener((buttonView, isChecked) -> eb92.alarmeSobreTensaoA = isChecked);

            cbAlarmeSobretensaoB = dialogModoTeste.findViewById(R.id.cb_alarme_sobretensao_b);
            cbAlarmeSobretensaoB.setChecked(alarmes.sobretensaoB);
            cbAlarmeSobretensaoB.setOnCheckedChangeListener((buttonView, isChecked) -> eb92.alarmeSobreTensaoB = isChecked);

            cbAlarmeSobretensaoC = dialogModoTeste.findViewById(R.id.cb_alarme_sobretensao_c);
            cbAlarmeSobretensaoC.setChecked(alarmes.sobretensaoC);
            cbAlarmeSobretensaoC.setOnCheckedChangeListener((buttonView, isChecked) -> eb92.alarmeSobreTensaoC = isChecked);

            cbAlarmeSubtensaoA = dialogModoTeste.findViewById(R.id.cb_alarme_subtensao_a);
            cbAlarmeSubtensaoA.setChecked(alarmes.subtensaoA);
            cbAlarmeSubtensaoA.setOnCheckedChangeListener((buttonView, isChecked) -> eb92.alarmeSubTensaoA = isChecked);

            cbAlarmeSubtensaoB = dialogModoTeste.findViewById(R.id.cb_alarme_subtensao_b);
            cbAlarmeSubtensaoB.setChecked(alarmes.subtensaoB);
            cbAlarmeSubtensaoB.setOnCheckedChangeListener((buttonView, isChecked) -> eb92.alarmeSubTensaoB = isChecked);

            cbAlarmeSubtensaoC = dialogModoTeste.findViewById(R.id.cb_alarme_subtensao_c);
            cbAlarmeSubtensaoC.setChecked(alarmes.subtensaoC);
            cbAlarmeSubtensaoC.setOnCheckedChangeListener((buttonView, isChecked) -> eb92.alarmeSubTensaoC = isChecked);

            cbEstadoTampa = dialogModoTeste.findViewById(R.id.cb_alarme_estado_tampa);
            cbEstadoTampa.setChecked(alarmes.estadoTampa);
            cbEstadoTampa.setOnCheckedChangeListener((buttonView, isChecked) -> eb92.alarmeEstadoTampa = isChecked);
        }
    }

    private void iniciaConfiguracaoNIC() {
        createDialogConfiguracaoNIC();
        enviaComandoEB17((byte) 0, MetodoNumeroSerial);
    }

    private void iniciaConfiguracao() {
        final Intent intentConfig = new Intent(this, ConfiguracaoMedidor.class);
        intentConfig.putExtra(Consts.EXTRAS_MEDIDORES1, medidores_ativos_1);
        intentConfig.putExtra(Consts.EXTRAS_MEDIDORES2, medidores_ativos_2);
        intentConfig.putExtra(Consts.EXTRAS_MEDIDORES3, medidores_ativos_3);
        intentConfig.putExtra(Consts.EXTRAS_MEDIDORES4, medidores_ativos_4);
        intentConfig.putExtra(Consts.EXTRAS_MEDIDORES5, medidores_ativos_5);
        intentConfig.putExtra(Consts.EXTRAS_MEDIDORES6, medidores_ativos_6);
        intentConfig.putExtra(Consts.EXTRAS_MEDIDORES7, medidores_ativos_7);
        intentConfig.putExtra(Consts.EXTRAS_MEDIDORES8, medidores_ativos_8);
        intentConfig.putExtra(Consts.EXTRAS_MEDIDORES, (Serializable) medidores);
        intentConfig.putExtra(Consts.EXTRAS_MEDIDORESCOM1, medidoresCom.numMedidor[0]);
        intentConfig.putExtra(Consts.EXTRAS_MEDIDORESCOM2, medidoresCom.numMedidor[1]);
        intentConfig.putExtra(Consts.EXTRAS_MEDIDORESCOM3, medidoresCom.numMedidor[2]);
        intentConfig.putExtra(Consts.EXTRAS_MEDIDORESCOM4, medidoresCom.numMedidor[3]);
        intentConfig.putExtra(Consts.EXTRAS_MEDIDORESCOM5, medidoresCom.numMedidor[4]);
        intentConfig.putExtra(Consts.EXTRAS_MEDIDORESCOM6, medidoresCom.numMedidor[5]);
        intentConfig.putExtra(Consts.EXTRAS_MEDIDORESCOM7, medidoresCom.numMedidor[6]);
        intentConfig.putExtra(Consts.EXTRAS_MEDIDORESCOM8, medidoresCom.numMedidor[7]);
        intentConfig.putExtra(Consts.EXTRAS_MEDIDORESCOM9, medidoresCom.numMedidor[8]);
        intentConfig.putExtra(Consts.EXTRAS_MEDIDORESCOM10, medidoresCom.numMedidor[9]);
        intentConfig.putExtra(Consts.EXTRAS_MEDIDORESCOM11, medidoresCom.numMedidor[10]);
        intentConfig.putExtra(Consts.EXTRAS_MEDIDORESCOM12, medidoresCom.numMedidor[11]);
        intentConfig.putExtra(Consts.EXTRAS_METODO_NUMERO_SERIAL, MetodoNumeroSerial);
        startActivity(intentConfig);
    }

    private void iniciaPredefinicaoNIC(){

        startActivity(new Intent(this, FileManagerNICActivity.class));

        Toast.makeText(this, "nada", Toast.LENGTH_SHORT);

    }

    private void iniciaMonitoramento() {
        showProgressBar("Buscando dados do medidor");
        codigoCanal = 0;
        enviaComandoEB11((byte) 0, MetodoNumeroSerial);
    }

    private void lancaCargaDePrograma() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        builder.setTitle("Carga de Programa");
        if (Arquivo.getCargasDePrograma().size() > 0) {
            mCarga = Arquivo.getCargaDeProgramaPorModelo(modeloMultiponto);
            if (mCarga == null) {
                builder.setMessage("Carga de programa para este medidor não encontrada");
                builder.setNegativeButton("Entendi", null);
            } else {
                if (mCarga.getArquivo() != null) {
                    if (!CargaController.testaVersaoERevisaoIgual(mCarga.getArquivo().getName(), versaoMedidor.split("\\."))) {
                        if (CargaController.testaVersaoERevisaoDiferente(mCarga.getArquivo().getName(), versaoMedidor.split("\\."))) {
                            builder.setMessage("Há uma atualização disponivel para este medidor. Versão: " + mCarga.getVersao() + "." + mCarga.getRevisao() + "\nDeseja Atualizar?\nA atualização é irreversivel.");
                            builder.setPositiveButton("Sim", (p1, p2) -> {
                                showProgressBar("Alterando protocolo de interface");
                                mTimeoutHandler.postDelayed(() -> {
                                    progressDialog.dismiss();
                                    mCarga = null;
                                    AlertDialog.Builder mBuild = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                                    mBuild.setTitle("Erro");
                                    mBuild.setMessage("Falha ao alterar protocolo de interface");
                                    mBuild.setNegativeButton("Entendi", null);
                                    mBuild.create();
                                    mBuild.show();
                                }, 3000);
                                enviarAB08();
                            });
                            builder.setNegativeButton("Não", null);
                        } else {
                            mCarga = null;
                            builder.setMessage("Versão de carga de programa local inválida.");
                            builder.setNegativeButton("Entendi", null);
                        }
                    } else {
                        builder.setMessage("Medidor já está atualizado com a carga de programa local");
                        builder.setNegativeButton("Entendi", null);
                    }
                } else {
                    mCarga = null;
                    Log.e("mTag", "Arquivo da carga de programa é nulo");
                    builder.setMessage("Não há cargas de programa baixadas");
                    builder.setNegativeButton("Entendi", null);
                }
            }
        } else {
            mCarga = null;
            builder.setMessage("Não há cargas de programa baixadas");
            builder.setNegativeButton("Entendi", null);
        }
        builder.create();
        builder.show();
    }

    private void iniciaMemoriaMassaQEE() {
        final View conf_mm = getLayoutInflater().inflate(R.layout.conf_mm_qee, null);

        final Spinner intervalo = conf_mm.findViewById(R.id.intervalo);
        String[] items = new String[]{"Conjunto Atual", "Conjunto 1", "Conjunto 2", "Conjunto 3", "Conjunto 4"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intervalo.setAdapter(adapter);
        intervalo.setSelection(0);

        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                android.R.style.Theme_Material_Dialog_Alert);
        AlertDialog alertDialog = builder
                .setTitle("Memória de Massa QEE")
                .setMessage("Informe o conjunto a ser lido")
                .setView(conf_mm)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        codigoCanal = intervalo.getSelectedItemPosition();
                        enviaComandoEB11((byte) 0x00, MetodoNumeroSerial);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        if (imm != null)
                            imm.hideSoftInputFromWindow(conf_mm.getWindowToken(), 0);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        if (imm != null)
                            imm.hideSoftInputFromWindow(conf_mm.getWindowToken(), 0);
                        // do nothing
                    }
                }).create();


        alertDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        alertDialog.show();
    }

    private void iniciaMemoriaMassaSM() {
        final View conf_mmsm = getLayoutInflater().inflate(R.layout.conf_mmsm, null);
        final EditText nome = conf_mmsm.findViewById(R.id.numero_dias);
        nome.setFilters(new InputFilter[]{new MinMaxFilter("0", "99")});

        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                android.R.style.Theme_Material_Dialog_Alert);
        AlertDialog alertDialog = builder
                .setTitle("Memória de Massa SM")
                .setMessage("Informe o número de dias a ser lido")
                .setView(conf_mmsm)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (nome.getText().toString().isEmpty()) {
                            nome.setText("0");
                        }
                        int dias = Integer.valueOf(nome.getText().toString());
                        IntervaloDiasMMSM = dias;
                        enviaComandoAB52((byte) 0x00, (byte) 0x05, dias, MetodoNumeroSerial);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        if (imm != null)
                            imm.hideSoftInputFromWindow(conf_mmsm.getWindowToken(), 0);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        if (imm != null)
                            imm.hideSoftInputFromWindow(conf_mmsm.getWindowToken(), 0);
                        // do nothing
                    }
                }).create();


        alertDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        alertDialog.show();
    }

    private void buscaQEEParametros() {
        codigoCanal = 0;
        enviaComandoEB11((byte) 0x00, MetodoNumeroSerial);
    }

    private void enviaComandoEB11(byte pacote, int metodo) {
        String num_medidor = "";
        if (tipo_medidor == TipoMedidor.ABSOLUTO) {
            num_medidor = medidorSelecionado.numero;
        }

        byte[] comando = new ComandoAbsoluto.EB11QEE()
                .comMedidorNumero(num_medidor, metodo)
                .build(pacote, (byte) codigoCanal, metodo);
        enviarComando(comando);

    }

    private void enviaComandoEB17(byte tipo_comando, int metodo) {
        String num_medidor = "";
        if (tipo_medidor == TipoMedidor.ABSOLUTO) {
            num_medidor = medidorSelecionado.numero;
        }

        byte[] comando = new ComandoAbsoluto.EB17_2()
                .comMedidorNumero(num_medidor, metodo)
                .build(tipo_comando, metodo);
        enviarComando(comando);

    }

    private void enviaComandoAB52(byte pacote, byte TipoLeitura, int Intervalo, int metodo) {
        String num_medidor = "";
        if (tipo_medidor == TipoMedidor.ABSOLUTO) {
            num_medidor = medidorSelecionado.numero;
        }

        byte[] comando = new ComandoAbsoluto.AB52()
                .comMedidorNumero(num_medidor, metodo)
                .build(pacote, TipoLeitura, Intervalo, metodo);
        enviarComando(comando);

    }

    private void iniciaMemoriaMassa(MedidorAbsoluto medidor) {
        final View conf_mm = getLayoutInflater().inflate(R.layout.conf_mm, null);
        final EditText nome = conf_mm.findViewById(R.id.numero_dias);
        nome.setFilters(new InputFilter[]{new MinMaxFilter("0", "99")});

        final Spinner canal = conf_mm.findViewById(R.id.numero_canal);
        String[] items = new String[]{"Energia Direta", "Energia Reversa", "Tensão", "Corrente", "THD Tensão", "THD Corrente"};
        if (tipo_medidor == TipoMedidor.EASY_VOLT) {
            items = new String[]{"Tensão", "THD Tensão", "Tensão Mínima", "Tensão Máxima"};
        } else if (tipo_medidor != TipoMedidor.EASY_TRAFO) {
            items = new String[]{"Energia Direta", "Energia Reversa", "Tensão", "Corrente"};
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this/*alertDialog.getContext()*/, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        canal.setAdapter(adapter);
        canal.setSelection(0);

        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                android.R.style.Theme_Material_Dialog_Alert);
        AlertDialog alertDialog = builder
                .setTitle("Memória de Massa")
                .setMessage("Dados da Memória de Massa")
                .setView(conf_mm)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (nome.getText().toString().isEmpty()) {
                            nome.setText("0");
                        }
                        int dias = Integer.valueOf(nome.getText().toString());
                        codigoCanal = canal.getSelectedItemPosition();
                        if (dias >= 0) {
                            String num_medidor = "";
                            if (tipo_medidor == TipoMedidor.ABSOLUTO) {
                                num_medidor = medidorSelecionado.numero;
                            }
                            byte[] comando = new ComandoAbsoluto.ABNT51()
                                    .comMedidorNumero(num_medidor)
                                    .build(dias, codigoCanal, (tipo_medidor == TipoMedidor.ABSOLUTO));
                            enviarComando(comando);
                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                            if (imm != null)
                                imm.hideSoftInputFromWindow(conf_mm.getWindowToken(), 0);
                        } else {
                            Toast.makeText(DeviceActivity.super.getApplicationContext(),
                                    "Informe o número de dias de 0 a 99.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        if (imm != null)
                            imm.hideSoftInputFromWindow(conf_mm.getWindowToken(), 0);
                        // do nothing
                    }
                }).create();


        alertDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        alertDialog.show();
    }

    private void iniciaAlteracaoIntervaloMM(MedidorAbsoluto medidor) {

        final View intervalo_mm = getLayoutInflater().inflate(R.layout.intervalo_item, null);
        final RadioGroup rg_IntervaloMM = intervalo_mm.findViewById(R.id.rg_intervaloMM);
        final RadioButton rb_Intervalo1MM = intervalo_mm.findViewById(R.id.rd1minuto);
        final RadioButton rb_Intervalo5MM = intervalo_mm.findViewById(R.id.rd5minutos);
        final RadioButton rb_Intervalo15MM = intervalo_mm.findViewById(R.id.rd15minutos);
        final Button btn_IntervaloMM = intervalo_mm.findViewById(R.id.btn_intervaloMM);

        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                android.R.style.Theme_Material_Dialog_Alert);
        AlertDialog alertDialog = builder
                .setTitle("Memória de Massa")
                .setMessage("Intervalo")
                .setView(intervalo_mm)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String num_medidor = "";
                        int MinutosMM = 1;

                        if (tipo_medidor == TipoMedidor.ABSOLUTO) {
                            num_medidor = medidorSelecionado.numero;
                        }

                        int radioButtonID = rg_IntervaloMM.getCheckedRadioButtonId();
                        View radioButton = rg_IntervaloMM.findViewById(radioButtonID);
                        int idx = rg_IntervaloMM.indexOfChild(radioButton);

                        switch(idx){
                            case 0:
                                MinutosMM = 1;
                                break;
                            case 1:
                                MinutosMM = 5;
                                break;
                            case 2:
                                MinutosMM = 15;
                                break;
                        }

                        leuEasyTrafo = false;
                        byte[] comando = new ComandoAbsoluto.ABNT73()
                                .comMedidorNumero(num_medidor)
                                .build(MinutosMM, (tipo_medidor == TipoMedidor.ABSOLUTO));
                        enviarComando(comando);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        if (imm != null)
                            imm.hideSoftInputFromWindow(intervalo_mm.getWindowToken(), 0);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        if (imm != null)
                            imm.hideSoftInputFromWindow(intervalo_mm.getWindowToken(), 0);
                        // do nothing
                    }
                }).create();


        alertDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        alertDialog.show();
    }

    private void ajustaDataHora(boolean data, MedidorAbsoluto medidor) {
        byte[] comando;
        Calendar localCalendar = Calendar.getInstance();
        String num_medidor = "";
        if (tipo_medidor == TipoMedidor.ABSOLUTO) {
            num_medidor = medidor.numero;
        }
        if (data) {
            comando = new ComandoAbsoluto.ABNT29()
                    .comMedidorNumero(num_medidor, MetodoNumeroSerial)
                    .build(localCalendar, (tipo_medidor == TipoMedidor.ABSOLUTO), MetodoNumeroSerial);
        } else {
            comando = new ComandoAbsoluto.ABNT30()
                    .comMedidorNumero(num_medidor, MetodoNumeroSerial)
                    .build(localCalendar, (tipo_medidor == TipoMedidor.ABSOLUTO), MetodoNumeroSerial);
        }
        enviarComando(comando);
    }

    private void alteracaoIntrvaloMM(MedidorAbsoluto medidor, byte Minutos) {
        byte[] comando = new ComandoAbsoluto.ABNT73()
                .comMedidorNumero(medidor.numero)
                .build(Minutos, (tipo_medidor == TipoMedidor.ABSOLUTO));
        enviarComando(comando);
    }

    private void exibirLeituraParametros(MedidorAbsoluto medidor) {
        String numero = "";
        String unidadeConsumidora = "";
        if (tipo_medidor == TipoMedidor.ABSOLUTO) {
            numero = medidor.numero;
            unidadeConsumidora = medidor.unidadeConsumidora;
        } else {
            numero = mNsMedidor;
            unidadeConsumidora = numMedidor;
        }
        Log.i("test->", numero);
        final Intent intent = new Intent(DeviceActivity.this, ResponseActivity.class);
        intent.putExtra(Consts.EXTRAS_NUMERO_MEDIDOR, numero);
        intent.putExtra(Consts.EXTRAS_UNIDADE_CONSUMIDORA, unidadeConsumidora);
        intent.putExtra(Consts.EXTRAS_DEVICE_NAME, mDeviceName);
        intent.putExtra(Consts.EXTRAS_EASY_TRAFO, (tipo_medidor != TipoMedidor.ABSOLUTO));
        intent.putExtra(Consts.EXTRAS_IS_SMFISCAL, strSMFiscal);
        intent.putExtra(Consts.EXTRAS_METODO_NUMERO_SERIAL, MetodoNumeroSerial);

        startActivity(intent);
    }

    private void exibirGrandezasInstantaneas(MedidorAbsoluto medidor) {
        String numero = "";
        String unidadeConsumidora = "";
        if (tipo_medidor == TipoMedidor.ABSOLUTO) {
            numero = medidor.numero;
            unidadeConsumidora = medidor.unidadeConsumidora;
        } else {
            numero = mNsMedidor;
            unidadeConsumidora = numMedidor;
        }
        final Intent intent = new Intent(DeviceActivity.this, PaginaFiscalActivity.class);
        intent.putExtra(Consts.EXTRAS_VERSAO_MEDIDOR, versaoMedidor);
        intent.putExtra(Consts.EXTRAS_NUMERO_MEDIDOR, numero);
        intent.putExtra(Consts.EXTRAS_UNIDADE_CONSUMIDORA, unidadeConsumidora);
        intent.putExtra(Consts.EXTRAS_DEVICE_NAME, mDeviceName);
        intent.putExtra(Consts.EXTRAS_EASY_TRAFO, (tipo_medidor != TipoMedidor.ABSOLUTO));
        intent.putExtra(Consts.EXTRAS_METODO_NUMERO_SERIAL, MetodoNumeroSerial);
        startActivity(intent);
    }

    private void trocaNomeUnidade(MedidorAbsoluto medidor) {
        final View alertText = getLayoutInflater().inflate(R.layout.alert_text, null);
        final EditText nome = alertText.findViewById(R.id.alert_text);
        nome.setFilters(new InputFilter[]{new InputFilter.LengthFilter(14)});
        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                android.R.style.Theme_Material_Dialog_Alert);
        AlertDialog alertDialog = builder
                .setTitle("Trocar Nome")
                .setMessage("Digite o nome do ponto")
                .setView(alertText)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String novo_nome = nome.getText().toString();
                        if (!novo_nome.trim().isEmpty()) {
                            byte[] comando = new ComandoAbsoluto.ABNT87()
                                    .comMedidorNumero(medidor.numero)
                                    .build(novo_nome, (tipo_medidor == TipoMedidor.ABSOLUTO));
                            enviarComando(comando);
                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                            if (imm != null)
                                imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);
                        } else {
                            Toast.makeText(DeviceActivity.super.getApplicationContext(),
                                    "Informe o novo nome",
                                    Toast.LENGTH_SHORT).show();
                        }
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

    private void resetRegistradores(MedidorAbsoluto medidor, byte tipo) {
        String titulo = "Reset de Registradores";
        String msg = "Você deseja realmente realizar o reset dos registradores?\r\nEsta operação é irreversível!";
        if (tipo == 2) {
            msg = "Você deseja realmente realizar o reset dos registradores de Memória de Massa?\r\nEsta operação é irreversível!";
            titulo = "Reset Memória de Massa";
        }
        else if (tipo == 3) {
            msg = "Você deseja realmente realizar o reset dos registradores de QEE?\r\nEsta operação é irreversível!";
            titulo = "Reset QEE";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                android.R.style.Theme_Material_Dialog_Alert);
        AlertDialog alertDialog = builder
                .setTitle(titulo)
                .setMessage(msg)
                .setPositiveButton("SIM", (dialog, which) -> {

                    byte[] comando = new ComandoAbsoluto.AB07ResetRegistradores()
                            .comMedidorNumero(medidor.numero)
                            .build((tipo_medidor == TipoMedidor.ABSOLUTO), tipo);
                    enviarComando(comando);

                    if (comando != null) {
                        Log.d("teste", Arrays.toString(comando));
                    }

                })
                .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        if (imm != null)
                            imm.hideSoftInputFromWindow(alertText.getWindowToken(), 0);*/
                        // do nothing
                    }
                }).create();
        /*alertDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);*/
        alertDialog.show();
    }

    private void NICPre(){
        ConfiguraçãoNICPre();
    }

    private void ConfiguraçãoNICPre(){
        dialogConfiguracaoNIC = new Dialog(this);
        dialogConfiguracaoNIC.setContentView(R.layout.dialog_configuracaonic_easy_trafo);
        if (dialogConfiguracaoNIC.getWindow() != null)
            dialogConfiguracaoNIC.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //someComBotoesDaConfiguracaoNIC();

        CheckBox cbTelefone1 = dialogConfiguracaoNIC.findViewById(R.id.cb_configuracaonic_telefone1_check);
        CheckBox cbTelefone2 = dialogConfiguracaoNIC.findViewById(R.id.cb_configuracaonic_telefone2_check);
        CheckBox cbTelefone3 = dialogConfiguracaoNIC.findViewById(R.id.cb_configuracaonic_telefone3_check);
        CheckBox cbTelefone4 = dialogConfiguracaoNIC.findViewById(R.id.cb_configuracaonic_telefone4_check);
        Switch tbPrateleira = dialogConfiguracaoNIC.findViewById(R.id.modo_prateleira);

        EditText titulo = dialogConfiguracaoNIC.findViewById(R.id.titulo);

        EditText etTelefone1 = dialogConfiguracaoNIC.findViewById(R.id.etTelefone1);
        EditText etTelefone2 = dialogConfiguracaoNIC.findViewById(R.id.etTelefone2);
        EditText etTelefone3 = dialogConfiguracaoNIC.findViewById(R.id.etTelefone3);
        EditText etTelefone4 = dialogConfiguracaoNIC.findViewById(R.id.etTelefone4);

        CheckBox cbAberturaTampa = dialogConfiguracaoNIC.findViewById(R.id.cb_abertura_tampa);
        CheckBox cbFaltaEnergia = dialogConfiguracaoNIC.findViewById(R.id.cb_falta_energia);
        CheckBox cbSubtensao = dialogConfiguracaoNIC.findViewById(R.id.cb_subtensao_ciclos);
        CheckBox cbSobretensao = dialogConfiguracaoNIC.findViewById(R.id.cb_sobretensao_ciclos);

        RadioButton rbCicloPadrao = dialogConfiguracaoNIC.findViewById(R.id.rb_ciclo_padrao);
        RadioButton rbCicloEtapa2 = dialogConfiguracaoNIC.findViewById(R.id.rb_etapa2);

        EditText etRepeticoes = dialogConfiguracaoNIC.findViewById(R.id.etRepeticoes);
        EditText etIntervalo1 = dialogConfiguracaoNIC.findViewById(R.id.etIntervalo1);
        EditText etIntervalo2 = dialogConfiguracaoNIC.findViewById(R.id.etIntervalo2);

        CheckBox cbTelefoneKeepAlive = dialogConfiguracaoNIC.findViewById(R.id.cb_Telefone_Keep_Alive);
        EditText etTelefoneKeepAlive = dialogConfiguracaoNIC.findViewById(R.id.etTelefoneKeepAlive);
        EditText etFrequenciaKeepAlive = dialogConfiguracaoNIC.findViewById(R.id.etFrequenciaKeepAlive);

        dialogConfiguracaoNIC.findViewById(R.id.etTelefone1).setOnClickListener(v -> {
            cbTelefone1.setEnabled(true);
        });

        dialogConfiguracaoNIC.findViewById(R.id.etTelefone2).setOnClickListener(v -> {
            cbTelefone2.setEnabled(true);
        });

        dialogConfiguracaoNIC.findViewById(R.id.etTelefone3).setOnClickListener(v -> {
            cbTelefone3.setEnabled(true);
        });

        dialogConfiguracaoNIC.findViewById(R.id.etTelefone4).setOnClickListener(v -> {
            cbTelefone4.setEnabled(true);
        });

        dialogConfiguracaoNIC.findViewById(R.id.etTelefoneKeepAlive).setOnClickListener(v -> {
            cbTelefoneKeepAlive.setEnabled(true);
        });

        dialogConfiguracaoNIC.findViewById(R.id.cb_configuracaonic_telefone1_check).setOnClickListener(v -> {
            bAlteracaoTelefoneDF = true;
            if(cbTelefone1.isChecked())
            {
                etTelefone1.setEnabled(true);
            }
            else
            {
                etTelefone1.setEnabled(false);
                etTelefone1.setText("");
            }
        });

        dialogConfiguracaoNIC.findViewById(R.id.cb_configuracaonic_telefone2_check).setOnClickListener(v -> {
            bAlteracaoTelefoneDF = true;
            if(cbTelefone2.isChecked())
            {
                etTelefone2.setEnabled(true);
            }
            else
            {
                etTelefone2.setEnabled(false);
                etTelefone2.setText("");
            }
        });

        dialogConfiguracaoNIC.findViewById(R.id.cb_configuracaonic_telefone3_check).setOnClickListener(v -> {
            bAlteracaoTelefoneDF = true;
            if(cbTelefone3.isChecked())
            {
                etTelefone3.setEnabled(true);
            }
            else
            {
                etTelefone3.setEnabled(false);
                etTelefone3.setText("");
            }
        });

        dialogConfiguracaoNIC.findViewById(R.id.cb_configuracaonic_telefone4_check).setOnClickListener(v -> {
            bAlteracaoTelefoneDF = true;
            if(cbTelefone4.isChecked())
            {
                etTelefone4.setEnabled(true);
            }
            else
            {
                etTelefone4.setEnabled(false);
                etTelefone4.setText("");
            }
        });

        dialogConfiguracaoNIC.findViewById(R.id.cb_Telefone_Keep_Alive).setOnClickListener(v -> {
            bAlteracaoTelefoneKeepAlive = true;
            if(cbTelefoneKeepAlive.isChecked())
            {
                etTelefoneKeepAlive.setEnabled(true);
            }
            else
            {
                etTelefoneKeepAlive.setEnabled(false);
                etTelefoneKeepAlive.setText("");
            }
        });

        dialogConfiguracaoNIC.findViewById(R.id.configuracaonic_btn_close).setOnClickListener(v -> dialogConfiguracaoNIC.dismiss());

        dialogConfiguracaoNIC.findViewById(R.id.configuracaonic_btn_forca_1).setOnClickListener(v -> {
            //mEB90.setNumeroTransformador(1);
            //enviaEB90();
        });

        dialogConfiguracaoNIC.findViewById(R.id.configuracaonic_btn_forca_2).setOnClickListener(v -> {
            //mEB90.setNumeroTransformador(2);
            //enviaEB90();
        });

        dialogConfiguracaoNIC.findViewById(R.id.configuracaonic_salvar_predefinição).setVisibility(GONE);

        String[] nome = FileManagerNICActivity.titulo.split(Pattern.quote("."));
        titulo.setText(nome[0]);

        if(!FileManagerNICActivity.resposta[0].equals("00000000000")) {
            cbTelefone1.setChecked(true);
            etTelefone1.setText(FileManagerNICActivity.resposta[0]);
        }

        if(!FileManagerNICActivity.resposta[1].equals("00000000000")) {
            cbTelefone2.setChecked(true);
            etTelefone2.setText(FileManagerNICActivity.resposta[1]);
        }

        if(!FileManagerNICActivity.resposta[2].equals("00000000000")) {
            cbTelefone3.setChecked(true);
            etTelefone3.setText(FileManagerNICActivity.resposta[2]);
        }

        if(!FileManagerNICActivity.resposta[3].equals("00000000000")) {
            cbTelefone4.setChecked(true);
            etTelefone4.setText(FileManagerNICActivity.resposta[3]);
        }

        if(FileManagerNICActivity.resposta[4].equals("1")){
            cbFaltaEnergia.setChecked(true);
        }

        if(FileManagerNICActivity.resposta[5].equals("1")){
            cbAberturaTampa.setChecked(true);
        }

        if(FileManagerNICActivity.resposta[6].equals("1")){
            cbSubtensao.setChecked(true);
        }

        if(FileManagerNICActivity.resposta[7].equals("1")){
            cbSobretensao.setChecked(true);
        }

        if(FileManagerNICActivity.resposta[8].equals("1")){
            rbCicloPadrao.setChecked(true);
        }
        else if(FileManagerNICActivity.resposta[8].equals("2")){
            rbCicloEtapa2.setChecked(true);
        }

        if(!FileManagerNICActivity.resposta[9].equals("0")){
            etRepeticoes.setText(FileManagerNICActivity.resposta[9]);
        }

        if(!FileManagerNICActivity.resposta[10].equals("0")){
            etIntervalo1.setText(FileManagerNICActivity.resposta[10]);
        }

        if(!FileManagerNICActivity.resposta[11].equals("0")){
            etIntervalo2.setText(FileManagerNICActivity.resposta[11]);
        }

        if(!FileManagerNICActivity.resposta[12].equals("00000000000")) {
            cbTelefoneKeepAlive.setChecked(true);
            etTelefoneKeepAlive.setText(FileManagerNICActivity.resposta[12]);
        }

        if(!FileManagerNICActivity.resposta[14].equals("0")){
            etFrequenciaKeepAlive.setText(FileManagerNICActivity.resposta[14]);
        }

        if(FileManagerNICActivity.resposta[15].equals("1")){
            tbPrateleira.setChecked(true);
        }

        dialogConfiguracaoNIC.findViewById(R.id.configuracaonic_enviar_alteracao).setOnClickListener(v -> {
            String Telefone1Aux = "00000000000";
            String Telefone2Aux = "00000000000";
            String Telefone3Aux = "00000000000";
            String Telefone4Aux = "00000000000";
            String TelefonesDFAux = Telefone1Aux + Telefone2Aux + Telefone3Aux + Telefone4Aux;
            String TelefoneKeepAliveAux = "00000000000";

            String etRepeticoesAux = "0";
            String etIntervalo1Aux = "0";
            String etIntervalo2Aux = "0";
            String etFrequenciaKeepAliveAux = "0";

            boolean bAlteracaoEventos = false;

            byte QtdTelefonesAux= 0;
            byte EventosAux = 0;
            byte RepeticoesAux = 0;
            int Intervalo1Aux = 0;
            int Intervalo2Aux = 0;
            byte Repeticoes = 0;
            int Intervalo1 = 0;
            int Intervalo2 = 0;
            byte FrequenciaKeepAliveAux = 0;
            byte TamCampo = 0;

            mEB17 = new ComandoAbsoluto.EB17().comNumerMedidor(monitoramentoNumeroEasy);
            mEB17.setLeitura(false);

            if(cbTelefone1.isChecked())
            {
                Telefone1Aux = etTelefone1.getText().toString();
                TamCampo = (byte)Telefone1Aux.length();
                if(TamCampo > 0)
                {
                    if(TamCampo < 11)
                    {
                        for(byte i = 0; i < (11-TamCampo);i++)
                        {
                            Telefone1Aux = "0" + Telefone1Aux;
                        }
                    }
                    else
                    {
                        Telefone1Aux = Telefone1Aux.substring(0,11);
                    }
                }
                else
                {
                    Telefone1Aux = "00000000000";
                }
                bAlteracaoTelefoneDF = true;
                QtdTelefonesAux++;
            }

            if(cbTelefone2.isChecked())
            {
                Telefone2Aux = etTelefone2.getText().toString();
                TamCampo = (byte)(Telefone2Aux.length());
                if(TamCampo > 0)
                {
                    if(TamCampo < 11)
                    {
                        for(byte i = 0; i < (11-TamCampo);i++)
                        {
                            Telefone2Aux = "0" + Telefone2Aux;
                        }
                    }
                    else
                    {
                        Telefone2Aux = Telefone2Aux.substring(0,11);
                    }
                }
                else
                {
                    Telefone2Aux = "00000000000";
                }
                bAlteracaoTelefoneDF = true;
                QtdTelefonesAux++;
            }

            if(cbTelefone3.isChecked())
            {
                Telefone3Aux = etTelefone3.getText().toString();
                TamCampo = (byte)(Telefone3Aux.length());
                if(TamCampo > 0)
                {
                    if(TamCampo < 11)
                    {
                        for(byte i = 0; i < (11-TamCampo);i++)
                        {
                            Telefone3Aux = "0" + Telefone3Aux;
                        }
                    }
                    else
                    {
                        Telefone3Aux = Telefone3Aux.substring(0,11);
                    }
                }
                else
                {
                    Telefone3Aux = "00000000000";
                }
                bAlteracaoTelefoneDF = true;
                QtdTelefonesAux++;
            }

            if(cbTelefone4.isChecked())
            {
                Telefone4Aux = etTelefone4.getText().toString();
                TamCampo = (byte)(Telefone4Aux.length());
                if(TamCampo > 0)
                {
                    if(TamCampo < 11)
                    {
                        for(byte i = 0; i < (11-TamCampo);i++)
                        {
                            Telefone4Aux = "0" + Telefone4Aux;
                        }
                    }
                    else
                    {
                        Telefone4Aux = Telefone4Aux.substring(0,11);
                    }
                }
                else
                {
                    Telefone4Aux = "00000000000";
                }
                bAlteracaoTelefoneDF = true;
                QtdTelefonesAux++;
            }

            if(!(sTelefone1.contentEquals(Telefone1Aux)) || !(sTelefone2.contentEquals(Telefone2Aux)) || !(sTelefone3.contentEquals(Telefone3Aux)) || !(sTelefone4.contentEquals(Telefone4Aux)))
            {
                // Algum dos telefones marcados
                bAlteracaoTelefoneDF = true;
            }
            else
            {
                // Nenhum telefone marcado
                bAlteracaoTelefoneDF = false;
            }

            if(bAlteracaoTelefoneDF)
            {
                mEB17.setAlteracaoTelefones(true);
                TelefonesDFAux = Telefone1Aux + Telefone2Aux + Telefone3Aux + Telefone4Aux;
                mEB17.setTelefonesDeteccaoFalhas(TelefonesDFAux);
                mEB17.setQtdTelefones(QtdTelefonesAux);
            }else
            {
                mEB17.setAlteracaoTelefones(false);
            }

            if(cbFaltaEnergia.isChecked())
            {
                bAlteracaoEventos = true;
                EventosAux |= 0x01;
            }
            else
            {
                EventosAux &= 0xFE;
            }

            if(cbSubtensao.isChecked())
            {
                bAlteracaoEventos = true;
                EventosAux |= 0x02;
            }
            else
            {
                EventosAux &= 0xFD;
            }

            if(cbSobretensao.isChecked())
            {
                bAlteracaoEventos = true;
                EventosAux |= 0x04;
            }
            else
            {
                EventosAux &= 0xFB;
            }

            if(cbAberturaTampa.isChecked())
            {
                bAlteracaoEventos = true;
                EventosAux |= 0x08;
            }
            else
            {
                EventosAux &= 0xF7;
            }

            if(iEventos != EventosAux)
            {
                // Houve mudança nos eventos
                bAlteracaoEventos = true;
            }
            else
            {
                bAlteracaoEventos = false;
            }

            if(bAlteracaoEventos)
            {
                mEB17.setAlteracaoEventos(true);
                mEB17.setEventos(EventosAux);
            }
            else
            {
                mEB17.setAlteracaoEventos(false);
            }

            if(rbCicloPadrao.isChecked() && (iCiclos == 0x02))
            {
                mEB17.setAlteracaoCiclos(true);
                mEB17.setCiclos((byte)0x00);
            }
            else if(rbCicloEtapa2.isChecked() && ((iCiclos == 0x00) || (iCiclos == 0x01)))
            {
                mEB17.setAlteracaoCiclos(true);
                mEB17.setCiclos((byte)0x02);
            }
            else
            {
                mEB17.setAlteracaoCiclos(false);
            }

            etRepeticoesAux = etRepeticoes.getText().toString();
            if(!etRepeticoesAux.isEmpty())
            {
                try {
                    byte d = Byte.parseByte(etRepeticoesAux);

                    if(iRepeticoes != d)
                    {
                        mEB17.setAlteracaoRepeticoes(true);
                        mEB17.setRepeticoesEtapa1(d);
                    }
                    else
                    {
                        mEB17.setAlteracaoRepeticoes(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Número de Repetições inválido", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                // Qtd de Repetições nao foi digitado
                mEB17.setAlteracaoRepeticoes(false);
                mEB17.setRepeticoesEtapa1((byte)0x00);
            }

            etIntervalo1Aux = etIntervalo1.getText().toString();
            if(!etIntervalo1Aux.isEmpty())
            {
                try {
                    int d = Integer.parseInt(etIntervalo1Aux);

                    if(iIntervalo1 != d)
                    {
                        mEB17.setAlteracaoIntervalo1(true);
                        mEB17.setIntervalo1(d);
                    }
                    else
                    {
                        mEB17.setAlteracaoIntervalo1(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Valor do Intervalo1 inválido", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                // Intervalo1 nao foi digitado
                mEB17.setAlteracaoIntervalo1(false);
                mEB17.setIntervalo1((int)0x00);
            }

            etIntervalo2Aux = etIntervalo2.getText().toString();
            if(!etIntervalo2Aux.isEmpty())
            {
                try {
                    int d = Integer.parseInt(etIntervalo2Aux);

                    if(iIntervalo2 != d)
                    {
                        mEB17.setAlteracaoIntervalo2(true);
                        mEB17.setIntervalo2(d);
                    }
                    else
                    {
                        mEB17.setAlteracaoIntervalo2(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Valor do Intervalo2 inválido", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                // Intervalo2 nao foi digitado
                mEB17.setAlteracaoIntervalo2(false);
                mEB17.setIntervalo2((int)0x00);
            }

            if(cbTelefoneKeepAlive.isChecked())
            {
                TelefoneKeepAliveAux = etTelefoneKeepAlive.getText().toString();
                TamCampo = (byte)(TelefoneKeepAliveAux.length());
                if(TamCampo > 0)
                {
                    if(TamCampo < 11)
                    {
                        for (byte i = 0; i < (11 - TamCampo); i++)
                        {
                            TelefoneKeepAliveAux = "0" + TelefoneKeepAliveAux;
                        }
                    }
                    else
                    {
                        TelefoneKeepAliveAux = TelefoneKeepAliveAux.substring(0,11);
                    }
                }
                else
                {
                    TelefoneKeepAliveAux = "00000000000";
                }

                //bAlteracaoTelefoneKeepAlive = true;
                //mEB17.setTelefoneKeepAlive(TelefoneKeepAliveAux);
                //mEB17.setAlteracaoTelefoneKeepAlive(true);
                mEB17.setValidade((byte)0x01);
            }
            else
            {
                //bAlteracaoTelefoneKeepAlive = true;
                //mEB17.setAlteracaoTelefoneKeepAlive(true);
                mEB17.setValidade((byte)0x00);
            }

            if(!sTelefoneKeepAlive.contentEquals(TelefoneKeepAliveAux))
            {
                // Houve alteração do telefone de keep alive
                bAlteracaoTelefoneKeepAlive = true;
                mEB17.setTelefoneKeepAlive(TelefoneKeepAliveAux);
                mEB17.setAlteracaoTelefoneKeepAlive(true);
            }
            else
            {
                // Não houve alteração de telefone de keep alive
                mEB17.setAlteracaoTelefoneKeepAlive(false);
            }

            etFrequenciaKeepAliveAux = etFrequenciaKeepAlive.getText().toString();
            if(!etFrequenciaKeepAliveAux.isEmpty())
            {
                try {
                    byte d = Byte.parseByte(etFrequenciaKeepAliveAux);

                    if(iFrequenciaKeepAlive != d)
                    {
                        mEB17.setAlteracaoFrequenciaKeepAlive(true);
                        mEB17.setFrequenciaKeepAlive(d);
                    }
                    else
                    {
                        mEB17.setAlteracaoFrequenciaKeepAlive(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Valor da Frequência Keep Alive inválida", Toast.LENGTH_SHORT).show();
                }
            }
            if(tbPrateleira.isChecked()){
                mEB17.setModoPrateleira((byte)0x01);
            }
            else {
                mEB17.setModoPrateleira((byte)0x00);
            }
            enviaEB17();
            bAlterandoConfigRemota = true;
            iContadorLeituraEB17Enviado = 0;

            AlertDialog dialog = new AlertDialog.Builder(this,
                    android.R.style.Theme_Material_Dialog_Alert)
                    .setTitle("Aviso")
                    .setMessage("Aguardando confirmação de alteração da configuração.")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false).setCancelable(false)
                    .setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .create();
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                private static final int AUTO_DISMISS_MILLIS = 180000;
                @Override
                public void onShow(final DialogInterface dialog) {
                    final Button defaultButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                    defaultButton.setClickable(false);
                    final CharSequence negativeButtonText = "";//defaultButton.getText();
                    new CountDownTimer(AUTO_DISMISS_MILLIS, 100) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if(bAlterandoConfigRemota == false){
                                iContadorLeituraEB17Enviado = 0;
                                bAlterandoConfigRemota = false;
                                dialog.cancel();
                            }
                            defaultButton.setText(String.format(Locale.getDefault(), "%s %d",negativeButtonText,
                                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1 //add one so it never displays zero
                            ));
                        }
                        @Override
                        public void onFinish() {
                            if (((AlertDialog) dialog).isShowing()) {
                                dialog.cancel();
                                if(bAlterandoConfigRemota){
                                    iContadorLeituraEB17Enviado = 0;
                                    bAlterandoConfigRemota = false;
                                    createDialogError("Erro ao alterar cfg da remota.");
                                    //Toast.makeText(((AlertDialog) dialog).getContext(), "Erro ao alterar cfg da remota.", Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(((AlertDialog) dialog).getContext(), "Sem resposta da Remota.", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            }
                        }
                    }.start();
                }
            });
            dialog.show();

            Toast.makeText(this, "Enviando alteração de configuração de NIC", Toast.LENGTH_SHORT).show();

            etTelefone1.setEnabled(true);
            etTelefone2.setEnabled(true);
            etTelefone3.setEnabled(true);
            etTelefone4.setEnabled(true);
            etTelefoneKeepAlive.setEnabled(true);

            cbTelefone1.setEnabled(true);
            cbTelefone2.setEnabled(true);
            cbTelefone3.setEnabled(true);
            cbTelefone4.setEnabled(true);
            cbTelefoneKeepAlive.setEnabled(true);
        });

        dialogConfiguracaoNIC.setCancelable(false);
        dialogConfiguracaoNIC.create();
        dialogConfiguracaoNIC.show();

        NIC = 0;
    }

    private void Prateleira(){
        alteracaoPrateleira();
        enviaComandoEB17((byte) 0, MetodoNumeroSerial);}

    private void alteracaoPrateleira(){
        String estado = "";

        if(iModoPrateleira == 0x00)
        {
            estado = "ativado";
        }

        if(iModoPrateleira == 0x01)
        {
            estado = "desativado";
        }

        AlertDialog.Builder builde = new AlertDialog.Builder(this);
        builde.setTitle("Alterar Modo Prateleira");
        builde.setMessage("Deseja ativar ou desativar o Modo Prateleira");


        builde.setNegativeButton("Ativar", (dialog, which) -> {
            AuxPrateleira = 1;

            String Telefone1Aux = "00000000000";
            String Telefone2Aux = "00000000000";
            String Telefone3Aux = "00000000000";
            String Telefone4Aux = "00000000000";
            String TelefonesDFAux = Telefone1Aux + Telefone2Aux + Telefone3Aux + Telefone4Aux;
            String TelefoneKeepAliveAux = "00000000000";

            String etRepeticoesAux = "0";
            String etIntervalo1Aux = "0";
            String etIntervalo2Aux = "0";
            String etFrequenciaKeepAliveAux = "0";

            boolean bAlteracaoEventos = false;
            boolean bAlteracaoPrateleira = false;

            byte QtdTelefonesAux= 0;
            byte EventosAux = 0;
            byte RepeticoesAux = 0;
            int Intervalo1Aux = 0;
            int Intervalo2Aux = 0;
            byte Repeticoes = 0;
            int Intervalo1 = 0;
            int Intervalo2 = 0;
            byte FrequenciaKeepAliveAux = 0;
            byte TamCampo = 0;

            mEB17 = new ComandoAbsoluto.EB17().comNumerMedidor(monitoramentoNumeroEasy);
            mEB17.setLeitura(false);


            mEB17.setAlteracaoTelefones(false);

            mEB17.setAlteracaoEventos(false);

            mEB17.setAlteracaoCiclos(false);

            mEB17.setAlteracaoRepeticoes(false);
            mEB17.setRepeticoesEtapa1((byte)0x00);

            mEB17.setAlteracaoIntervalo1(false);
            mEB17.setIntervalo1((int)0x00);

            mEB17.setAlteracaoIntervalo2(false);
            mEB17.setIntervalo2((int)0x00);

            mEB17.setValidade((byte)0x00);

            mEB17.setAlteracaoTelefoneKeepAlive(false);

            mEB17.setAlteracaoFrequenciaKeepAlive(false);


            mEB17.setAlteracaoModoPrateleira(true);
            mEB17.setModoPrateleira((byte)0x00);

            enviaEB17();
            bAlterandoConfigRemota = true;
            iContadorLeituraEB17Enviado = 0;

                    AlertDialog dialo = new AlertDialog.Builder(this,
                            android.R.style.Theme_Material_Dialog_Alert)
                            .setTitle("Aviso")
                            .setMessage("Aguardando confirmação de alteração da configuração.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setCancelable(false).setCancelable(false)
                            .setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .create();
                    dialo.setOnShowListener(new DialogInterface.OnShowListener() {
                        private static final int AUTO_DISMISS_MILLIS = 180000;
                        @Override
                        public void onShow(final DialogInterface dialog) {
                            final Button defaultButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                            defaultButton.setClickable(false);
                            final CharSequence negativeButtonText = "";//defaultButton.getText();
                            new CountDownTimer(AUTO_DISMISS_MILLIS, 100) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    if(bAlterandoConfigRemota == false){
                                        iContadorLeituraEB17Enviado = 0;
                                        bAlterandoConfigRemota = false;
                                        dialog.cancel();
                                    }
                                    defaultButton.setText(String.format(Locale.getDefault(), "%s %d",negativeButtonText,
                                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1 //add one so it never displays zero
                                    ));
                                }
                                @Override
                                public void onFinish() {
                                    if (((AlertDialog) dialog).isShowing()) {
                                        dialog.cancel();
                                        if(bAlterandoConfigRemota){
                                            iContadorLeituraEB17Enviado = 0;
                                            bAlterandoConfigRemota = false;
                                            createDialogError("Erro ao alterar cfg da remota.");
                                            //Toast.makeText(((AlertDialog) dialog).getContext(), "Erro ao alterar cfg da remota.", Toast.LENGTH_SHORT).show();
                                            //Toast.makeText(((AlertDialog) dialog).getContext(), "Sem resposta da Remota.", Toast.LENGTH_SHORT).show();
                                            dialog.cancel();
                                        }
                                        else{
                                            if(bAlterandoConfigRemota){
                                                bAlterandoConfigRemota = false;
                                                iContadorLeituraEB17Enviado = 0;
                                                Toast.makeText(((AlertDialog) dialog).getContext(), "Alteração concluída", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }
                            }.start();
                        }
                    });
                    dialo.show();

                    Toast.makeText(this, "Enviando alteração de configuração de NIC", Toast.LENGTH_SHORT).show();

            AuxPrateleira = 0;

        }
        ).setPositiveButton("Desativar", (dialog, which) -> {

            AuxPrateleira = 1;

            String Telefone1Aux = "00000000000";
            String Telefone2Aux = "00000000000";
            String Telefone3Aux = "00000000000";
            String Telefone4Aux = "00000000000";
            String TelefonesDFAux = Telefone1Aux + Telefone2Aux + Telefone3Aux + Telefone4Aux;
            String TelefoneKeepAliveAux = "00000000000";

            String etRepeticoesAux = "0";
            String etIntervalo1Aux = "0";
            String etIntervalo2Aux = "0";
            String etFrequenciaKeepAliveAux = "0";

            boolean bAlteracaoEventos = false;
            boolean bAlteracaoPrateleira = false;

            byte QtdTelefonesAux= 0;
            byte EventosAux = 0;
            byte RepeticoesAux = 0;
            int Intervalo1Aux = 0;
            int Intervalo2Aux = 0;
            byte Repeticoes = 0;
            int Intervalo1 = 0;
            int Intervalo2 = 0;
            byte FrequenciaKeepAliveAux = 0;
            byte TamCampo = 0;

            mEB17 = new ComandoAbsoluto.EB17().comNumerMedidor(monitoramentoNumeroEasy);
            mEB17.setLeitura(false);


            mEB17.setAlteracaoTelefones(false);

            mEB17.setAlteracaoEventos(false);

            mEB17.setAlteracaoCiclos(false);

            mEB17.setAlteracaoRepeticoes(false);
            mEB17.setRepeticoesEtapa1((byte)0x00);

            mEB17.setAlteracaoIntervalo1(false);
            mEB17.setIntervalo1((int)0x00);

            mEB17.setAlteracaoIntervalo2(false);
            mEB17.setIntervalo2((int)0x00);

            mEB17.setValidade((byte)0x00);

            mEB17.setAlteracaoTelefoneKeepAlive(false);

            mEB17.setAlteracaoFrequenciaKeepAlive(false);


            mEB17.setAlteracaoModoPrateleira(true);
            mEB17.setModoPrateleira((byte)0x01);

            enviaEB17();
            bAlterandoConfigRemota = true;
            iContadorLeituraEB17Enviado = 0;

            AlertDialog dialo = new AlertDialog.Builder(this,
                    android.R.style.Theme_Material_Dialog_Alert)
                    .setTitle("Aviso")
                    .setMessage("Aguardando confirmação de alteração da configuração.")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false).setCancelable(false)
                    .setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .create();
            dialo.setOnShowListener(new DialogInterface.OnShowListener() {
                private static final int AUTO_DISMISS_MILLIS = 180000;
                @Override
                public void onShow(final DialogInterface dialog) {
                    final Button defaultButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                    defaultButton.setClickable(false);
                    final CharSequence negativeButtonText = "";//defaultButton.getText();
                    new CountDownTimer(AUTO_DISMISS_MILLIS, 100) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if(bAlterandoConfigRemota == false){
                                iContadorLeituraEB17Enviado = 0;
                                bAlterandoConfigRemota = false;
                                dialog.cancel();
                            }
                            defaultButton.setText(String.format(Locale.getDefault(), "%s %d",negativeButtonText,
                                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1 //add one so it never displays zero
                            ));
                        }
                        @Override
                        public void onFinish() {
                            if (((AlertDialog) dialog).isShowing()) {
                                dialog.cancel();
                                if(bAlterandoConfigRemota){
                                    iContadorLeituraEB17Enviado = 0;
                                    bAlterandoConfigRemota = false;
                                    createDialogError("Erro ao alterar cfg da remota.");
                                    //Toast.makeText(((AlertDialog) dialog).getContext(), "Erro ao alterar cfg da remota.", Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(((AlertDialog) dialog).getContext(), "Sem resposta da Remota.", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                                else{
                                    if(bAlterandoConfigRemota){
                                        bAlterandoConfigRemota = false;
                                        iContadorLeituraEB17Enviado = 0;
                                        Toast.makeText(((AlertDialog) dialog).getContext(), "Alteração concluída", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    }.start();
                }
            });
            dialo.show();

            Toast.makeText(this, "Enviando alteração de configuração de NIC", Toast.LENGTH_SHORT).show();

            AuxPrateleira = 0;

        });
        builde.create();
        builde.show();

        AlertDialog dialo = new AlertDialog.Builder(this,
                android.R.style.Theme_Material_Dialog_Alert)
                .setTitle("Aviso")
                .setMessage("Aguardando confirmação de alteração pendente.")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false).setCancelable(false)
                .setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialo, int which) {
                        dialo.cancel();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .create();
        dialo.setOnShowListener(new DialogInterface.OnShowListener() {
            private static final int AUTO_DISMISS_MILLIS = 180000;
            @Override
            public void onShow(final DialogInterface dialo) {
                final Button defaultButton = ((AlertDialog) dialo).getButton(AlertDialog.BUTTON_NEGATIVE);
                defaultButton.setClickable(false);
                final CharSequence negativeButtonText = "";//defaultButton.getText();
                new CountDownTimer(AUTO_DISMISS_MILLIS, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if(bAlterandoConfigRemota == false){
                            iContadorLeituraEB17Enviado = 0;
                            bAlterandoConfigRemota = false;
                            dialo.cancel();
                        }
                        defaultButton.setText(String.format(Locale.getDefault(), "%s %d",negativeButtonText,
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1 //add one so it never displays zero
                        ));
                    }
                    @Override
                    public void onFinish() {
                        if (((AlertDialog) dialo).isShowing()) {
                            dialo.cancel();
                            if(bAlterandoConfigRemota){
                                iContadorLeituraEB17Enviado = 0;
                                bAlterandoConfigRemota = false;
                                createDialogError("Erro ao alterar cfg da remota.");
                                //Toast.makeText(((AlertDialog) dialo).getContext(), "Erro ao alterar cfg da remota.", Toast.LENGTH_SHORT).show();
                                //Toast.makeText(((AlertDialog) dialog).getContext(), "Sem resposta da Remota.", Toast.LENGTH_SHORT).show();
                                dialo.cancel();
                            }
                        }
                    }
                }.start();
            }
        });
        dialo.show();
    }

    private void iniciaCalibracao(){
        createDialogCalibracao();
    }

    private void createDialogError(String mensagem){

        AlertDialog error = new AlertDialog.Builder(this,
                android.R.style.Theme_Material_Dialog_Alert)
                .setTitle("Aviso")
                .setMessage(mensagem)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false).setCancelable(false)
                .setNeutralButton("Entendi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface error, int which) {
                        error.cancel();
                    }
                })
                .create();

        error.show();

    }

    private void createDialogCalibracao(){
        dialogCalibracao = new Dialog(this);
        dialogCalibracao.setContentView(R.layout.dialog_calibracao_easy_trafo_l);
        if (dialogCalibracao.getWindow() != null)
            dialogCalibracao.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RadioButton ativa = dialogCalibracao.findViewById(R.id.rb_ativa);
        RadioButton reativa = dialogCalibracao.findViewById(R.id.rb_reativa);


        if(iCalibracao == 0x05){
            reativa.setChecked(true);
            ativa.setChecked(false);

        }
        if(iCalibracao == 0x00){
            reativa.setChecked(false);
            ativa.setChecked(true);
        }

        dialogCalibracao.findViewById(R.id.calibracao_btn_close).setOnClickListener(v -> dialogCalibracao.dismiss());

        dialogCalibracao.findViewById(R.id.calibracao_enviar).setOnClickListener(v ->{
            mAB05 = new ComandoAbsoluto.AB05().comNumerMedidor(monitoramentoNumeroEasy, MetodoNumeroSerial);
            if(ativa.isChecked()){
                mAB05.setCalibracao((byte)0x00);
                itest = true;
            }
            if(reativa.isChecked()){
                mAB05.setCalibracao((byte)0x05);
                itest = true;
            }



            mAB05.setModoEspecial((byte)0x01);
            enviarAB05(MetodoNumeroSerial);

            Toast.makeText(this, "Enviando alteração de configuração de NIC", Toast.LENGTH_SHORT).show();

            Toast.makeText(this,"Alteração concluída", Toast.LENGTH_SHORT).show();

            dialogCalibracao.dismiss();

        });

        dialogCalibracao.findViewById(R.id.calibracao_ler).setOnClickListener(v ->{
            mAB05 = new ComandoAbsoluto.AB05().comNumerMedidor(monitoramentoNumeroEasy, MetodoNumeroSerial);
            if(iCalibracao == 0x05){
                reativa.setChecked(true);
                ativa.setChecked(false);

            }
            if(iCalibracao == 0x00){
                reativa.setChecked(false);
                ativa.setChecked(true);
            }

            mAB05.setModoEspecial((byte)0x00);
            enviarAB05(MetodoNumeroSerial);


        });

        dialogCalibracao.setCancelable(false);
        dialogCalibracao.create();
        dialogCalibracao.show();


    }

    private class MeterEasyTrafoAdapter
            extends RecyclerView.Adapter<MeterEasyTrafoAdapter.MeterETViewHolder> {

        private String numero_medidor = "";
        private String ns_medidor = "";
        private String data_hora = "";

        public class MeterETViewHolder extends RecyclerView.ViewHolder {
            private final OnClickListener incidenteClick = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //medidorSelecionado = medidores.get(getAdapterPosition());
                    exibirMenu(v);
                }
            };

            TextView unidadeConsumidora;
            TextView unidadeConsumidoraInfo;
            TextView nmrMedidorInfo;
            TextView nmrMedidor;
            TextView dataHora;
            TextView dataHoraInfo;
            TextView versao;
            TextView versaoInfo;
            TextView modComTitulo;
            TextView modComInfo;
            TextView fases;
            TextView moduloNomeTitulo;
            TextView moduloNome;
            TextView moduloTipoTitulo;
            TextView moduloTipoInfo;
            TextView moduloEstadoTitulo;
            TextView moduloEstado;
            TextView moduloSinalTitulo;
            TextView moduloSinal;
            ImageView statusMedidor;

            public MeterETViewHolder(@NonNull View view) {
                super(view);
                unidadeConsumidora = view.findViewById(R.id.unidade_consumidora);
                unidadeConsumidoraInfo = view.findViewById(R.id.unidade_consumidora_info);
                nmrMedidorInfo = view.findViewById(R.id.mi_tv_dadoMedidorTrafoInfo);
                nmrMedidor = view.findViewById(R.id.mi_tv_dadoMedidorTrafo);
                dataHora = view.findViewById(R.id.dados_medidor);
                dataHoraInfo = view.findViewById(R.id.dados_medidor_info);
                versao = view.findViewById(R.id.medidor_versao);
                versaoInfo = view.findViewById(R.id.medidor_versao_info);
                modComTitulo = view.findViewById(R.id.modCom_versao_info_title);
                modComInfo = view.findViewById(R.id.mi_tv_modCom_versao);
                fases = view.findViewById(R.id.numero_fases_medidor);
                fases.setVisibility(LinearLayout.GONE);
                statusMedidor = view.findViewById(R.id.status_medidor);
                statusMedidor.setVisibility(LinearLayout.GONE);
                unidadeConsumidora.setOnClickListener(incidenteClick);
                view.findViewById(R.id.incidente_medidor).setOnClickListener(incidenteClick);
                unidadeConsumidora.setOnClickListener(incidenteClick);
                moduloNome = view.findViewById(R.id.mi_tv_modulo_nome);
                moduloNomeTitulo = view.findViewById(R.id.mi_tv_modulo_nome_title);
                moduloTipoTitulo = view.findViewById(R.id.mi_tv_tipo_nome_title);
                moduloTipoInfo = view.findViewById(R.id.mi_tv_tipo_modcom);
                moduloEstado = view.findViewById(R.id.mi_tv_modulo_estado);
                moduloEstadoTitulo = view.findViewById(R.id.mi_tv_modulo_estado_title);
                moduloSinal = view.findViewById(R.id.mi_tv_modulo_sinal);
                moduloSinalTitulo = view.findViewById(R.id.mi_tv_modulo_sinal_title);
            }
        }


        @NonNull
        @Override
        public MeterETViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.meter_item, viewGroup, false);
            return new MeterETViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MeterETViewHolder meterViewHolder, int i) {
            medidorSelecionado = new MedidorAbsoluto();
            medidorSelecionado.numero = ns_medidor;

            meterViewHolder.unidadeConsumidoraInfo.setText("Ponto:");
            meterViewHolder.unidadeConsumidora.setText(numero_medidor);

            meterViewHolder.nmrMedidorInfo.setVisibility(VISIBLE);
            meterViewHolder.nmrMedidor.setVisibility(VISIBLE);
            meterViewHolder.nmrMedidor.setText(medidorSelecionado.numero);
            monitoramentoNumeroEasy = medidorSelecionado.numero;

            meterViewHolder.dataHoraInfo.setText("Data e Hora:");
            meterViewHolder.dataHora.setText(data_hora);
            meterViewHolder.versaoInfo.setText("Versão:");
            meterViewHolder.versao.setText(versaoMedidor);

            if (!tipoModulo.isEmpty()) {
                if(tipo_medidor == TipoMedidor.EASY_VOLT){
                    meterViewHolder.modComTitulo.setVisibility(VISIBLE);
                    meterViewHolder.modComInfo.setVisibility(VISIBLE);
                    meterViewHolder.moduloTipoTitulo.setVisibility(VISIBLE);
                    meterViewHolder.moduloTipoInfo.setVisibility(VISIBLE);
                    meterViewHolder.modComInfo.setText(versaoModCom);
                    meterViewHolder.moduloTipoInfo.setText(tipoModCom);
                }
                else{
                    meterViewHolder.modComTitulo.setVisibility(GONE);
                    meterViewHolder.modComInfo.setVisibility(GONE);
                    meterViewHolder.moduloTipoTitulo.setVisibility(GONE);
                    meterViewHolder.moduloTipoInfo.setVisibility(GONE);
                }
                meterViewHolder.moduloNomeTitulo.setVisibility(VISIBLE);
                meterViewHolder.moduloNome.setVisibility(VISIBLE);
                meterViewHolder.moduloEstado.setVisibility(VISIBLE);
                meterViewHolder.moduloEstadoTitulo.setVisibility(VISIBLE);
                meterViewHolder.moduloSinal.setVisibility(VISIBLE);
                meterViewHolder.moduloSinalTitulo.setVisibility(VISIBLE);

                meterViewHolder.moduloNome.setText(tipoModulo);
                meterViewHolder.moduloEstado.setText(estadoModulo);
                meterViewHolder.moduloSinal.setText(sinalModulo+" dBm");
            }


            SimpleDateFormat smf = new SimpleDateFormat("dd/MM/yyyy' 'HH:mm:ss");
            Date dt = new Date();
            try {
                dt = smf.parse(data_hora);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long diff = System.currentTimeMillis() - dt.getTime();
            long seconds = diff / 1000;

            if (seconds < 0) {
                seconds *= -1;
            }

            if (seconds > 300) {
                meterViewHolder.dataHora.setTextColor(Color.RED);
            }

        }

        public MeterEasyTrafoAdapter(String medidor, String datahora, String numero) {
            this.data_hora = datahora;
            this.numero_medidor = medidor;
            this.ns_medidor = numero;
        }

        @Override
        public int getItemCount() {
            return 1;
        }
    }
}