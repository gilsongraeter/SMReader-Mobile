package com.starmeasure.protocoloabsoluto;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RespostaAbsoluto {

    public byte[] mData;
    private ArrayList<byte[]> mRespostaComposta;
    private boolean ehRespostaSimples;

    public class RegistradoresAtuais {
        public long TotalizadorGeralEnergiaAtivaDireta;
        public long TotalizadorEnergiaAtivaDiretaPonta;
        public long TotalizadorEnergiaAtivaDiretaForaPonta;
        public long TotalizadorEnergiaAtivaDiretaReservado;
        public long TotalizadorEnergiaAtivaDiretaTarifaD;

        public long TotalizadorGeralEnergiaAtivaReversa;
        public long TotalizadorEnergiaAtivaReversaPonta;
        public long TotalizadorEnergiaAtivaReversaForaPonta;
        public long TotalizadorEnergiaAtivaReversaReservado;
        public long TotalizadorEnergiaAtivaReversaTarifaD;

        public long TotalizadorGeralEnergiaReativaPositiva;
        public long TotalizadorEnergiaReativaPositivaPonta;
        public long TotalizadorEnergiaReativaPositivaForaPonta;
        public long TotalizadorEnergiaReativaPositivaReservado;
        public long TotalizadorEnergiaReativaPositivaTarifaD;

        public long TotalizadorGeralEnergiaReativaNegativa;
        public long TotalizadorEnergiaReativaNegativaPonta;
        public long TotalizadorEnergiaReativaNegativaForaPonta;
        public long TotalizadorEnergiaReativaNegativaReservado;
        public long TotalizadorEnergiaReativaNegativaTarifaD;

        public long TotalizadorEnergiaAtivaDiretaSaidaPrimeiroElemento;
        public long TotalizadorEnergiaAtivaReversaSaidaPrimeiroElemento;
        public long TotalizadorEnergiaAtivaDiretaSaidaSegundoElemento;
        public long TotalizadorEnergiaAtivaReversaSaidaSegundoElemento;
        public long TotalizadorEnergiaAtivaDiretaSaidaTerceiroElemento;
        public long TotalizadorEnergiaAtivaReversaSaidaTerceiroElemento;

        public String VersaoSoftware;
    }

    public class GrandezasInstantaneas {
        public String horario;
        public String versao;
        public float temperatura;
        public float VA;
        public float VB;
        public float VC;
        public float VAB;
        public float VBC;
        public float VCA;
        public float IA;
        public float IB;
        public float IC;
        public float IN;
        public float anguloVA;
        public float anguloVB;
        public float anguloVC;
        public float defasagemIA;
        public float defasagemIB;
        public float defasagemIC;
        public float frequencia;
        public float PA;
        public float PB;
        public float PC;
        public float PT;
        public float QA;
        public float QB;
        public float QC;
        public float QT;
        public float SA;
        public float SB;
        public float SC;
        public float ST;
        public float FPA;
        public float FPB;
        public float FPC;
        public float FPT;
        public float cosphiA;
        public float cosphiB;
        public float cosphiC;
        public float cosphiT;
        public float DHT_A;
        public float DHT_B;
        public float DHT_C;
        public float DHC_A;
        public float DHC_B;
        public float DHC_C;
        public String EstadoTampa;
    }

    public class Alarmes {
        public boolean sobretensaoA;
        public boolean sobretensaoB;
        public boolean sobretensaoC;
        public boolean subtensaoA;
        public boolean subtensaoB;
        public boolean subtensaoC;
        public boolean estadoTampa;
    }

    private static long ByteArrayToLong(byte[] data, int first, int length) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < length; i++)
            s.append(String.format("%02X", 0xff & data[first + i]));
        return Long.valueOf(s.toString());
    }

    public static float ByteArrayToFloat(byte[] data, int first) {
        ByteBuffer bb = ByteBuffer.allocate(4);
        for (int i = first; i < first + 4; i++)
            bb.put(data[i]);
        bb.position(0);
        return bb.order(ByteOrder.LITTLE_ENDIAN).getFloat();
    }

    public long ByteArrayToLong(byte[] data, int first) {
        int valor = ((data[first + 1] << 8) & 0x0000ff00) | (data[first] & 0x000000ff);

        return (long) valor;
    }

    public short byteArrayToShort(int first) {
        ByteBuffer buffer = ByteBuffer.wrap(new byte[]{mData[first], mData[first + 1]});
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        return buffer.getShort();
    }

    public boolean isAberturaSessao() {
        return mData[0] == ProtocoloAbsoluto.ComandoAB
                && mData[5] == ProtocoloAbsoluto.SolicitacaoAberturaSessaoAutenticada;
    }

    public boolean isGrandezasInstantaneas() {
        return mData[0] == ProtocoloAbsoluto.AbntLeituraGrandezasInstantaneas;
    }

    public boolean isRegistradoresAtuaisEasyTrafo() {
        return mData[0] == ProtocoloAbsoluto.ComandoEB
                && mData[5] == ProtocoloAbsoluto.AbntLeituraRegistradoresAtuaisEasyTrafo;
    }

    public boolean isRegistradoresAtuais() {
        return mData[0] == ProtocoloAbsoluto.ComandoAB
                && mData[5] == ProtocoloAbsoluto.AbntLeituraRegistradoresAtuais;
    }

    public boolean isRespostaOcorrencias() {
        return mData[0] == ProtocoloAbsoluto.AbntLimpezaOcorrenciasMedidor;
    }

    public boolean isRespostaData() {
        return mData[0] == ProtocoloAbsoluto.AbntAlteracaoData;
    }

    public boolean isRespostaHora() {
        return mData[0] == ProtocoloAbsoluto.AbntAlteracaoHora;
    }

    public boolean isRespostaReset() {
        return mData[0] == ProtocoloAbsoluto.ComandoEB
                && mData[5] == ProtocoloAbsoluto.ResetRegistradores;
    }

    public boolean isRespostaLeituraParametros() {
        return mData[0] == ProtocoloAbsoluto.AbntLeituraParametros;
    }

    public boolean isRespostaMemoriaMassaQEE() {
        return mData[0] == ProtocoloAbsoluto.ComandoEB
                && mData[5] == ProtocoloAbsoluto.MemoriaMassaQEE;
    }

    public boolean isRespostaMemoriaMassaSM() {
        return mData[0] == ProtocoloAbsoluto.ComandoAB
                && mData[5] == ProtocoloAbsoluto.AbntLeituraMemoriaMassa;
    }

    public boolean isRespostaMemoriaMassa() {
        return mData[0] == ProtocoloAbsoluto.AbntLeituraMemoriaMassa;
    }

    public boolean isLeituraStatusMedidor() {
        return mData[0] == ProtocoloAbsoluto.ComandoAB
                && mData[5] == ProtocoloAbsoluto.AlteracaoCorteReligamento
                && mData[6] == 0x00;
    }

    public boolean isCorteReligamento() {
        return mData[0] == ProtocoloAbsoluto.ComandoAB
                && mData[5] == ProtocoloAbsoluto.AlteracaoCorteReligamento
                && mData[6] == 0x01;
    }

    public String getNumeroMedidor() {
        return String.format("%02X%02X%02X%02X",
                mData[1], mData[2], mData[3], mData[4]);
    }

    public byte getCodigoOcorrencia() {
        if (isOcorrencia())
            return mData[5];
        return 0;
    }

    public String getSufixoNumeroMedidor() {
        return String.format("%02X%02X%02X",
                mData[2], mData[3], mData[4]);
    }

    public LeituraStatusMedidor interpretarLeituraStatusMedidor() {
        LeituraStatusMedidor leituraStatusMedidor = new LeituraStatusMedidor();
        leituraStatusMedidor.EstadoUnidadeConsumidora = mData[7];
        return leituraStatusMedidor;
    }

    public Alarmes processaAlarmes() {
        Alarmes alarmes = new Alarmes();
        byte b255 = mData[255];
        alarmes.sobretensaoA = (b255 & (1 << 1)) != 0;
        alarmes.sobretensaoB = (b255 & (1 << 2)) != 0;
        alarmes.sobretensaoC = (b255 & (1 << 3)) != 0;
        alarmes.subtensaoA = (b255 & (1 << 4) ) != 0;
        alarmes.subtensaoB = (b255 & (1 << 5)) != 0;
        alarmes.subtensaoC = (b255 & (1 << 6)) != 0;
        alarmes.estadoTampa = (b255 & (1 << 7)) != 0;

        return alarmes;
    }

    private int[] convertCharArrayToIntArray(char[] list) {
        int[] num = new int[list.length];
        for (int i = 0; i < list.length; i++) {
            num[i] = list[i];
        }
        return num;
    }

    public LeituraDados51 interpretaResposta51() {
        LeituraDados51 leituraDados51 = new LeituraDados51();
        leituraDados51.pacoteAtual = 1;
        leituraDados51.textUltimoPeriodo = String.format("%02X/%02X/20%02X %02X:%02X:%02X",
                mData[15], mData[16], mData[17], mData[12], mData[13], mData[14]);
        leituraDados51.textUltimaFatura = String.format("%02X/%02X/20%02X %02X:%02X:%02X",
                mData[21], mData[22], mData[23], mData[18], mData[19], mData[20]);

        leituraDados51.ultimoPeriodo = Calendar.getInstance();
        leituraDados51.ultimoPeriodo.set(ProtocoloAbsoluto.BCDToByte(mData[17]) + 2000, ProtocoloAbsoluto.BCDToByte(mData[16]) - 1, ProtocoloAbsoluto.BCDToByte(mData[15]),
                ProtocoloAbsoluto.BCDToByte(mData[12]), ProtocoloAbsoluto.BCDToByte(mData[13]), ProtocoloAbsoluto.BCDToByte(mData[14]));

        leituraDados51.periodoIntegracao = ProtocoloAbsoluto.BCDToByte(mData[203]);

        leituraDados51.numeroPalavras = ByteArrayToLong(mData, 74, 3);
        leituraDados51.totalGrandezas = leituraDados51.numeroPalavras / 3;

        leituraDados51.ultimoPeriodo.add(Calendar.MINUTE, (int) ((leituraDados51.totalGrandezas * leituraDados51.periodoIntegracao) - leituraDados51.periodoIntegracao) * -1);


        float valor = (float) leituraDados51.numeroPalavras / 166;
        leituraDados51.numeroPacotes = (long) valor;
        if ((valor % 1) > 0) {
            leituraDados51.numeroPacotes++;
        }

        leituraDados51.numeradorCanal1 = ByteArrayToLong(mData, 128, 3);
        leituraDados51.denominadorCanal1 = ByteArrayToLong(mData, 131, 3);
        leituraDados51.numeradorCanal2 = ByteArrayToLong(mData, 134, 3);
        leituraDados51.denominadorCanal2 = ByteArrayToLong(mData, 137, 3);
        leituraDados51.numeradorCanal3 = ByteArrayToLong(mData, 140, 3);
        leituraDados51.denominadorCanal3 = ByteArrayToLong(mData, 143, 3);

        //cal.set(2013,2,7,15,42,30);
        return leituraDados51;
    }

    public boolean interpretaResposta52() {

        return (mData[5] & 0x10) != 0;

    }

    public LeituraAB03 interpretaAB03(){
        LeituraAB03 leitura = new LeituraAB03();

        leitura.StatusParametrizacao = mData[6];
        leitura.StatusAtivacao = mData[7];
        leitura.UC = mData[11] & 0xFF;
        leitura.UC <<= 8;
        leitura.UC |= mData[10] & 0xFF;
        leitura.UC <<= 8;
        leitura.UC |= mData[9] & 0xFF;
        leitura.UC <<= 8;
        leitura.UC |= mData[8] & 0xFF;

        //leitura.UC = mData[11];
        //leitura.UC |= (int) (mData[10]) << 8;
        //leitura.UC |= (int) (mData[9]) << 16;
        //leitura.UC |= (int) (mData[8]) << 24;
        leitura.IU = (int) (mData[16]);
        leitura.IU |= mData[15] << 8;
        leitura.IU |= mData[14] << 16;
        leitura.IU |= mData[13] << 24;
        leitura.IU |= mData[12] << 32;
        leitura.IUTC = mData[21];
        leitura.IUTC |= mData[20] << 8;
        leitura.IUTC |= mData[19] << 16;
        leitura.IUTC |= mData[18] << 24;
        leitura.IUTC |= mData[17] << 32;
        leitura.Sequenciador = mData[22];
        leitura.CodigoErro = mData[254];
        leitura.SubCodigoErro = mData[255];

        return leitura;
    }

    public LeituraAB04 interpretaAB04(){
        LeituraAB04 leitura = new LeituraAB04();

        leitura.TipoComando = mData[6];
        leitura.CodMostrador[0] = mData[7];
        leitura.CodMostrador[1] = mData[8];
        leitura.CodMostrador[2] = mData[9];
        leitura.CodMostrador[3] = mData[10];
        leitura.CodMostrador[4] = mData[11];
        leitura.CodMostrador[5] = mData[12];
        leitura.CodMostrador[6] = mData[13];
        leitura.CodMostrador[7] = mData[14];
        leitura.CodMostrador[8] = mData[15];
        leitura.CodigoErro = mData[254];
        leitura.SubCodigoErro = mData[255];

        return leitura;
    }

    public LeituraAB05 interpretaAB05(){
        LeituraAB05 leitura = new LeituraAB05();

        leitura.test = false;
        leitura.ModoEspecial = mData[6];
        leitura.Calibracao = mData[8];


        return leitura;
    }

    public LeituraEB17 interpretaEB17() {
        LeituraEB17 leitura = new LeituraEB17();

        leitura.isLeitura = mData[6] == 0;

        leitura.ValeAlteracao = mData[7];
        leitura.ValeAlteracao2 = mData[8];

        leitura.TelefonesDeteccaoFalhas = String.format("%02X", mData[9]);
        leitura.TelefonesDeteccaoFalhas += String.format("%02X", mData[10]);
        leitura.TelefonesDeteccaoFalhas += String.format("%02X", mData[11]);
        leitura.TelefonesDeteccaoFalhas += String.format("%02X", mData[12]);
        leitura.TelefonesDeteccaoFalhas += String.format("%02X", mData[13]);
        leitura.TelefonesDeteccaoFalhas += String.format("%02X", mData[14]);
        leitura.TelefonesDeteccaoFalhas += String.format("%02X", mData[15]);
        leitura.TelefonesDeteccaoFalhas += String.format("%02X", mData[16]);
        leitura.TelefonesDeteccaoFalhas += String.format("%02X", mData[17]);
        leitura.TelefonesDeteccaoFalhas += String.format("%02X", mData[18]);
        leitura.TelefonesDeteccaoFalhas += String.format("%02X", mData[19]);
        leitura.TelefonesDeteccaoFalhas += String.format("%02X", mData[20]);
        leitura.TelefonesDeteccaoFalhas += String.format("%02X", mData[21]);
        leitura.TelefonesDeteccaoFalhas += String.format("%02X", mData[22]);
        leitura.TelefonesDeteccaoFalhas += String.format("%02X", mData[23]);
        leitura.TelefonesDeteccaoFalhas += String.format("%02X", mData[24]);
        leitura.TelefonesDeteccaoFalhas += String.format("%02X", mData[25]);
        leitura.TelefonesDeteccaoFalhas += String.format("%02X", mData[26]);
        leitura.TelefonesDeteccaoFalhas += String.format("%02X", mData[27]);
        leitura.TelefonesDeteccaoFalhas += String.format("%02X", mData[28]);
        leitura.TelefonesDeteccaoFalhas += String.format("%02X", mData[29]);
        leitura.TelefonesDeteccaoFalhas += String.format("%02X", mData[30]);

        leitura.Telefone1 = leitura.TelefonesDeteccaoFalhas.substring(0,11);
        leitura.Telefone2 = leitura.TelefonesDeteccaoFalhas.substring(11,22);
        leitura.Telefone3 = leitura.TelefonesDeteccaoFalhas.substring(22, 33);
        leitura.Telefone4 = leitura.TelefonesDeteccaoFalhas.substring(33,44);

        leitura.QtdTelefones = mData[31];
        leitura.Eventos = mData[32];
        leitura.Ciclos = mData[33];
        leitura.RepeticoesEtapa1 = mData[34];

        leitura.Intervalo1 = BufferTo16(mData, 35);
        leitura.Intervalo2 = BufferTo16(mData, 37);

        leitura.TelefoneKeepAlive = String.format("%02X", mData[39]);
        leitura.TelefoneKeepAlive += String.format("%02X", mData[40]);
        leitura.TelefoneKeepAlive += String.format("%02X", mData[41]);
        leitura.TelefoneKeepAlive += String.format("%02X", mData[42]);
        leitura.TelefoneKeepAlive += String.format("%02X", mData[43]);
        String TelAux = String.format("%02X", mData[44]);
        leitura.TelefoneKeepAlive += TelAux.substring(0,1);

        leitura.Validade = mData[45];
        leitura.FrequenciaKeepAlive = mData[46];
        leitura.ModoPrateleira = mData[47];

        return leitura;
    }

    public ComandoAbsoluto.LeituraConfiguracaoMedidor interpretaConfiguracaoMedidor(){
        ComandoAbsoluto.LeituraConfiguracaoMedidor leitura = new ComandoAbsoluto.LeituraConfiguracaoMedidor();

        leitura.Item = mData[20];

        return leitura;
    }

    public static final int BufferTo8(byte[] b, int i)
    {
        int i8 = b[i] & 0xFF;
        return i8;
    }

    public static final int BufferTo16(byte[] b, int i)
    {
        int i16 = 0;
        i16 |= b[i+1] & 0xFF;
        i16 <<= 8;
        i16 |= b[i] & 0xFF;
        return i16;
    }

    public LeituraEB90 interpretaEB90() {
        LeituraEB90 leitura = new LeituraEB90();

        leitura.isLeitura = mData[6] == 0;

        leitura.transformadorLido = mData[7];

        leitura.potenciaNominal = byteArrayToShort(8);

        leitura.valorTemperaturaNivel1 = byteArrayToShort(11) / 100.0;
        leitura.valorTemperaturaNivel2 = byteArrayToShort(13) / 100.0;
        leitura.expoenteDoOleo = byteArrayToShort(15) / 100.0;
        leitura.expoenteDoEnrolamento = byteArrayToShort(17) / 100.0;
        leitura.constanteDeTempoDoOleo = byteArrayToShort(19) / 100.0;
        leitura.constanteDeTempoDoEnrolamento = byteArrayToShort(21) / 100.0;
        leitura.constanteDoModeloTermico11 = byteArrayToShort(23) / 100.0;
        leitura.constanteDoModeloTermico21 = byteArrayToShort(25) / 100.0;
        leitura.constanteDoModeloTermico22 = byteArrayToShort(27) / 100.0;
        leitura.constanteDeDiscretizacao = byteArrayToShort(29) / 100.0;
        leitura.relacaoDePerdasDeCargaNaCorrenteNominalParaPerdasEmVazio = byteArrayToShort(31) / 100.0;
        leitura.temperaturaDoPontoMaisQuenteNominal = byteArrayToShort(33) / 100.0;
        leitura.elevacaoDeTemperaturaDoTopoDoOleo = byteArrayToShort(35) / 100.0;
        leitura.gradienteObtidoPelaDiferencaEntreTemperaturaDoPontoMaisQuenteEDoTopoDoOleo = byteArrayToShort(37) / 100.0;
        leitura.nivel1DeAlarmeParaSobretemperaturaDoPontoMaisQuenteDoTransformador = byteArrayToShort(39) / 100.0;
        leitura.nivel2DeAlarmeParaSobretemperaturaDoPontoMaisQuenteDoTransformador = byteArrayToShort(41) / 100.0;
        leitura.nivelSobrecorrenteFaseA = byteArrayToShort(43) / 10.0;
        leitura.nivelSobrecorrenteFaseB = byteArrayToShort(45) / 10.;
        leitura.nivelSobrecorrenteFaseC = byteArrayToShort(47) / 10.0;
        leitura.nivelSobretensaoFaseA = byteArrayToShort(49) / 10;
        leitura.nivelSobretensaoFaseB = byteArrayToShort(51) / 10;
        leitura.nivelSobretensaoFaseC = byteArrayToShort(53) / 10;
        leitura.nivelSubtensaoFaseA = byteArrayToShort(55) / 10;
        leitura.nivelSubtensaoFaseB = byteArrayToShort(57) / 10;
        leitura.nivelSubtensaoFaseC = byteArrayToShort(59) / 10;

        leitura.temPapeltermoestabilizado = mData[10] != 0;

        return leitura;
    }


    public LeituraCabecalhoQEE interpretaRespostaAB11Cabecalho() {
        LeituraCabecalhoQEE leituraDados = new LeituraCabecalhoQEE();
        leituraDados.pacoteAtual = 1;


        leituraDados.textdataInicio = String.format("%02X/%02X/20%02X %02X:%02X:%02X",
                mData[13], mData[14], mData[15], mData[10], mData[11], mData[12]);
        leituraDados.textdataFim = String.format("%02X/%02X/20%02X %02X:%02X:%02X",
                mData[19], mData[20], mData[21], mData[16], mData[17], mData[18]);

        leituraDados.dataInicio = Calendar.getInstance();
        leituraDados.dataInicio.set(ProtocoloAbsoluto.BCDToByte(mData[15]) + 2000, ProtocoloAbsoluto.BCDToByte(mData[14]) - 1, ProtocoloAbsoluto.BCDToByte(mData[13]),
                ProtocoloAbsoluto.BCDToByte(mData[10]), ProtocoloAbsoluto.BCDToByte(mData[11]), ProtocoloAbsoluto.BCDToByte(mData[12]));

        leituraDados.dataFim = Calendar.getInstance();
        leituraDados.dataFim.set(ProtocoloAbsoluto.BCDToByte(mData[21]) + 2000, ProtocoloAbsoluto.BCDToByte(mData[20]) - 1, ProtocoloAbsoluto.BCDToByte(mData[19]),
                ProtocoloAbsoluto.BCDToByte(mData[16]), ProtocoloAbsoluto.BCDToByte(mData[17]), ProtocoloAbsoluto.BCDToByte(mData[18]));

        leituraDados.intevaloQEE = ByteArrayToLong(mData, 22);
        leituraDados.numeroRegistros = ByteArrayToLong(mData, 24);
        leituraDados.numeroRegistrosValidos = ByteArrayToLong(mData, 26);
        leituraDados.grandezas = mData[28];
        leituraDados.DRP = ByteArrayToFloat(mData, 29);
        leituraDados.DRC = ByteArrayToFloat(mData, 33);
        leituraDados.DTT95 = ByteArrayToFloat(mData, 37);
        leituraDados.FD95 = ByteArrayToFloat(mData, 41);
        leituraDados.tensaoReferencia = ByteArrayToFloat(mData, 45);
        leituraDados.constanteMultiplicacaoFrequencia = ByteArrayToFloat(mData, 49);
        leituraDados.constanteMultiplicacaoTemperatura = ByteArrayToFloat(mData, 53);
        leituraDados.constanteMultiplicacaoTensao = ByteArrayToFloat(mData, 57);
        leituraDados.constanteMultiplicacaoDesequilibrio = ByteArrayToFloat(mData, 61);
        leituraDados.constanteMultiplicacaoHarmonicas = ByteArrayToFloat(mData, 65);
        leituraDados.constanteMultiplicacaoFatorPotencia = ByteArrayToFloat(mData, 69);
        leituraDados.percentualTensaoPrecariaSuperior = ByteArrayToFloat(mData, 73);
        leituraDados.percentualTensaoPrecariaInferior = ByteArrayToFloat(mData, 77);
        leituraDados.percentualTensaoCriticaSuperior = ByteArrayToFloat(mData, 81);
        leituraDados.percentualTensaoCriticaInferior = ByteArrayToFloat(mData, 85);
        leituraDados.tipoLigacao = mData[89];

        if((leituraDados.grandezas & 0xFF) == 0xD7) {
            leituraDados.numeroBytes = (int) leituraDados.numeroRegistros * 31;
        }
        else{
            leituraDados.numeroBytes = (int) leituraDados.numeroRegistros * 23;
        }
        leituraDados.posicaoAtual = 0;
        float valor = (float) leituraDados.numeroBytes / 247;
        leituraDados.numeroPacotes = (long) valor;
        if ((valor % 1) > 0) {
            leituraDados.numeroPacotes++;
        }

        return leituraDados;
    }

    public LeituraCabecalhoMMSMBloco1 interpretaRespostaAB52CabecalhoBloco1() {
        LeituraCabecalhoMMSMBloco1 leituraDadosBloco1 = new LeituraCabecalhoMMSMBloco1();
        leituraDadosBloco1.pacoteAtual = 1;

        leituraDadosBloco1.IU = (int) (mData[13]) << 32;
        leituraDadosBloco1.IU |= (int) (mData[12]) << 24;
        leituraDadosBloco1.IU |= (int) (mData[11]) << 16;
        leituraDadosBloco1.IU |= (int) (mData[10]) << 8;
        leituraDadosBloco1.IU |= mData[9];
        leituraDadosBloco1.tipoEstrutura = mData[14];
        leituraDadosBloco1.tamanhoEstrutura = mData[15];
        leituraDadosBloco1.kWh = (int) (mData[19]) << 24;
        leituraDadosBloco1.kWh |= (int) (mData[18]) << 16;
        leituraDadosBloco1.kWh |= (int) (mData[17]) << 8;
        leituraDadosBloco1.kWh |= mData[16];
        leituraDadosBloco1.kV = (int) (mData[23]) << 24;
        leituraDadosBloco1.kV |= (int) (mData[22]) << 16;
        leituraDadosBloco1.kV |= (int) (mData[21]) << 8;
        leituraDadosBloco1.kV |= mData[20];
        leituraDadosBloco1.kA = (int) (mData[27]) << 24;
        leituraDadosBloco1.kA |= (int) (mData[26]) << 16;
        leituraDadosBloco1.kA |= (int) (mData[25]) << 8;
        leituraDadosBloco1.kA |= mData[24];
        leituraDadosBloco1.kTHD = (int) (mData[31]) << 24;
        leituraDadosBloco1.kTHD |= (int) (mData[30]) << 16;
        leituraDadosBloco1.kTHD |= (int) (mData[29]) << 8;
        leituraDadosBloco1.kTHD |= mData[28];
        leituraDadosBloco1.kpWh = (int) (mData[35]) << 24;
        leituraDadosBloco1.kpWh |= (int) (mData[34]) << 16;
        leituraDadosBloco1.kpWh |= (int) (mData[33]) << 8;
        leituraDadosBloco1.kpWh |= mData[32];
        leituraDadosBloco1.kVp = (int) (mData[39]) << 24;
        leituraDadosBloco1.kVp |= (int) (mData[38]) << 16;
        leituraDadosBloco1.kVp |= (int) (mData[37]) << 8;
        leituraDadosBloco1.kVp |= mData[36];
        leituraDadosBloco1.kAp = (int) (mData[43]) << 24;
        leituraDadosBloco1.kAp |= (int) (mData[42]) << 16;
        leituraDadosBloco1.kAp |= (int) (mData[41]) << 8;
        leituraDadosBloco1.kAp |= mData[40];

        leituraDadosBloco1.numeroPacotes = 247 / leituraDadosBloco1.tamanhoEstrutura;
        leituraDadosBloco1.posicaoAtual = 0;

        return leituraDadosBloco1;
    }

    public LeituraCabecalhoMMSM interpretaRespostaAB52Cabecalho() {
        LeituraCabecalhoMMSM leituraDados = new LeituraCabecalhoMMSM();

        leituraDados.DataHora = (int) (mData[12]) << 24;
        leituraDados.DataHora |= (int) (mData[11]) << 16;
        leituraDados.DataHora |= (int) (mData[10]) << 8;
        leituraDados.DataHora |= ((int) (mData[9]) & 0xFF);

        for(byte x = 13, i=0; x < 29; x+=5){
            leituraDados.RegEnergia[i] = (int) (mData[(x + 3)]) << 24;
            leituraDados.RegEnergia[i] |= (int) (mData[(x + 2)]) << 16;
            leituraDados.RegEnergia[i] |= (int) (mData[(x + 1)]) << 8;
            leituraDados.RegEnergia[i] |= ((int) (mData[x]) & 0xFF);
            i++;
        }

        for(byte x = 29, i=0; x < 41; x+=0, i++){
            for(byte y = 0, s=0; y < 3; y++, s++){
                leituraDados.Grandezas[i][s] = (short) ((short) (mData[(x + 1)]) << 8);
                leituraDados.Grandezas[i][s] |= (short)(mData[x++] & 0xFF);
                x++;
            }
        }
        leituraDados.flags = mData[41];
        leituraDados.crc = (short) ((short) (mData[42]) << 8);
        leituraDados.crc |= (short)(mData[43] & 0xFF);

        return leituraDados;
    }

    public boolean interpretaRespostaEB11() {

        if ((mData[7] & 0x10) != 0) {
            return true;
        } else {
            return false;
        }
        /*
        if (mData[7] != 0) {
            return true;
        }
        else {
            return false;
        }*/

    }

    public boolean interpretaRespostaAB52() {

        if ((mData[7] & 0x10) != 0) {
            return true;
        } else {
            return false;
        }
        /*
        if (mData[7] != 0) {
            return true;
        }
        else {
            return false;
        }*/
    }

    public LeituraQEE preparaQEE() {
        return new LeituraQEE();
    }

    public LeituraQEESM2 preparaQEESM2() {
        return new LeituraQEESM2();
    }

    public LeituraQEE preparaMMSM() {
        return new LeituraQEE();
    }

    public LeituraDados52 prepara52() {
        return new LeituraDados52();
    }

    public RegistradoresAtuais interpretarRegistradoresAtuais(boolean isEasyTrafo) {
        RegistradoresAtuais registradoresAtuais = new RegistradoresAtuais();

        registradoresAtuais.TotalizadorGeralEnergiaAtivaDireta = ByteArrayToLong(mData, 7, 5);
        registradoresAtuais.TotalizadorEnergiaAtivaDiretaPonta = ByteArrayToLong(mData, 12, 5);
        registradoresAtuais.TotalizadorEnergiaAtivaDiretaForaPonta = ByteArrayToLong(mData, 17, 5);
        registradoresAtuais.TotalizadorEnergiaAtivaDiretaReservado = ByteArrayToLong(mData, 22, 5);
        registradoresAtuais.TotalizadorEnergiaAtivaDiretaTarifaD = ByteArrayToLong(mData, 27, 5);

        registradoresAtuais.TotalizadorGeralEnergiaAtivaReversa = ByteArrayToLong(mData, 32, 5);
        registradoresAtuais.TotalizadorEnergiaAtivaReversaPonta = ByteArrayToLong(mData, 37, 5);
        registradoresAtuais.TotalizadorEnergiaAtivaReversaForaPonta = ByteArrayToLong(mData, 42, 5);
        registradoresAtuais.TotalizadorEnergiaAtivaReversaReservado = ByteArrayToLong(mData, 47, 5);
        registradoresAtuais.TotalizadorEnergiaAtivaReversaTarifaD = ByteArrayToLong(mData, 52, 5);

        registradoresAtuais.TotalizadorGeralEnergiaReativaPositiva = ByteArrayToLong(mData, 57, 5);
        registradoresAtuais.TotalizadorEnergiaReativaPositivaPonta = ByteArrayToLong(mData, 62, 5);
        if (!isEasyTrafo) {
            registradoresAtuais.TotalizadorEnergiaReativaPositivaForaPonta = ByteArrayToLong(mData, 67, 5);
            registradoresAtuais.TotalizadorEnergiaReativaPositivaReservado = ByteArrayToLong(mData, 72, 5);
            registradoresAtuais.TotalizadorEnergiaReativaPositivaTarifaD = ByteArrayToLong(mData, 77, 5);

            registradoresAtuais.TotalizadorGeralEnergiaReativaNegativa = ByteArrayToLong(mData, 82, 5);
            registradoresAtuais.TotalizadorEnergiaReativaNegativaPonta = ByteArrayToLong(mData, 87, 5);
            registradoresAtuais.TotalizadorEnergiaReativaNegativaForaPonta = ByteArrayToLong(mData, 92, 5);
            registradoresAtuais.TotalizadorEnergiaReativaNegativaReservado = ByteArrayToLong(mData, 97, 5);
            registradoresAtuais.TotalizadorEnergiaReativaNegativaTarifaD = ByteArrayToLong(mData, 102, 5);

            registradoresAtuais.TotalizadorEnergiaAtivaDiretaSaidaPrimeiroElemento = ByteArrayToLong(mData, 107, 5);
            registradoresAtuais.TotalizadorEnergiaAtivaReversaSaidaPrimeiroElemento = ByteArrayToLong(mData, 112, 5);
            registradoresAtuais.TotalizadorEnergiaAtivaDiretaSaidaSegundoElemento = ByteArrayToLong(mData, 117, 5);
            registradoresAtuais.TotalizadorEnergiaAtivaReversaSaidaSegundoElemento = ByteArrayToLong(mData, 122, 5);
            registradoresAtuais.TotalizadorEnergiaAtivaDiretaSaidaTerceiroElemento = ByteArrayToLong(mData, 127, 5);
            registradoresAtuais.TotalizadorEnergiaAtivaReversaSaidaTerceiroElemento = ByteArrayToLong(mData, 132, 5);

        }

        registradoresAtuais.VersaoSoftware = String.format("%02X.%02X",
                mData[148], mData[149]);


        return registradoresAtuais;
    }

    public GrandezasInstantaneas interpretarGrandezasInstantaneas() {
        GrandezasInstantaneas g = new GrandezasInstantaneas();
        g.horario = String.format("%02X/%02X/20%02X %02X:%02X:%02X",
                mData[8], mData[9], mData[10], mData[5], mData[6], mData[7]);
        g.VA = ByteArrayToFloat(mData, 11);
        g.VB = ByteArrayToFloat(mData, 15);
        g.VC = ByteArrayToFloat(mData, 19);
        g.VAB = ByteArrayToFloat(mData, 23);
        g.VBC = ByteArrayToFloat(mData, 27);
        g.VCA = ByteArrayToFloat(mData, 31);
        g.IA = ByteArrayToFloat(mData, 35);
        g.IB = ByteArrayToFloat(mData, 39);
        g.IC = ByteArrayToFloat(mData, 43);
        g.IN = ByteArrayToFloat(mData, 47);
        g.PA = ByteArrayToFloat(mData, 51);
        g.PB = ByteArrayToFloat(mData, 55);
        g.PC = ByteArrayToFloat(mData, 59);
        g.PT = ByteArrayToFloat(mData, 63);
        g.QA = ByteArrayToFloat(mData, 67);
        g.QB = ByteArrayToFloat(mData, 71);
        g.QC = ByteArrayToFloat(mData, 75);
        g.QT = ByteArrayToFloat(mData, 79);
        g.SA = ByteArrayToFloat(mData, 99);
        g.SB = ByteArrayToFloat(mData, 103);
        g.SC = ByteArrayToFloat(mData, 107);
        g.ST = ByteArrayToFloat(mData, 111);
        g.cosphiA = ByteArrayToFloat(mData, 131);
        g.cosphiB = ByteArrayToFloat(mData, 135);
        g.cosphiC = ByteArrayToFloat(mData, 139);
        g.cosphiT = ByteArrayToFloat(mData, 143);
        g.FPA = ByteArrayToFloat(mData, 151);
        g.FPB = ByteArrayToFloat(mData, 155);
        g.FPC = ByteArrayToFloat(mData, 159);
        g.FPT = ByteArrayToFloat(mData, 163);
        g.defasagemIA = ByteArrayToFloat(mData, 167);
        g.defasagemIB = ByteArrayToFloat(mData, 171);
        g.defasagemIC = ByteArrayToFloat(mData, 175);
        g.temperatura = ByteArrayToFloat(mData, 179);
        g.frequencia = ByteArrayToFloat(mData, 183);
        g.anguloVA = ByteArrayToFloat(mData, 197);
        g.anguloVB = ByteArrayToFloat(mData, 201);
        g.anguloVC = ByteArrayToFloat(mData, 205);
        g.DHT_A = ByteArrayToFloat(mData, 221);
        g.DHT_B = ByteArrayToFloat(mData, 225);
        g.DHT_C = ByteArrayToFloat(mData, 229);
        g.DHC_A = ByteArrayToFloat(mData, 233);
        g.DHC_B = ByteArrayToFloat(mData, 237);
        g.DHC_C = ByteArrayToFloat(mData, 241);
        if((mData[251] & 0x01) == 0x01){
            // Tampa aberta
            g.EstadoTampa = " Aberta";
        }
        else
        {
            // Tampa Fechada
            g.EstadoTampa = " Fechada";
        }
        return g;
    }

    public RespostaAbsoluto(ArrayList<byte[]> RespostaComposta) {
        mRespostaComposta = RespostaComposta;
        mData = mRespostaComposta.get(0);
        ehRespostaSimples = false;
    }

    public RespostaAbsoluto(byte[] data) {
        mData = data;
        ehRespostaSimples = true;

    }

    public boolean isOcorrencia() {
        return mData[0] == ProtocoloAbsoluto.Ocorrencia;
    }

    public boolean isAb08() {
        return mData[0] == ProtocoloAbsoluto.ComandoAB && mData[5] == ProtocoloAbsoluto.AB08;
    }

    public boolean isEB17() {
        return mData[0] == ProtocoloAbsoluto.ComandoEB && mData[5] == ProtocoloAbsoluto.EB17;
    }

    public boolean isEB90() {
        return mData[0] == ProtocoloAbsoluto.ComandoEB && mData[5] == ProtocoloAbsoluto.EB90;
    }

    public boolean isEB92() {
        return mData[0] == ProtocoloAbsoluto.ComandoEB && mData[5] == ProtocoloAbsoluto.EB92;
    }

    public boolean isAbnt21() {
        return mData[0] == ProtocoloAbsoluto.AbntLeituraRegistradoresAtuais;
    }

    public boolean isAB03(){
        return  mData[0] == ProtocoloAbsoluto.ComandoAB && mData[5] == ProtocoloAbsoluto.AB03;
    }

    public boolean isAB04(){
        return  mData[0] == ProtocoloAbsoluto.ComandoAB && mData[5] == ProtocoloAbsoluto.AB04;
    }

    public boolean isAB05(){
        return  mData[0] == ProtocoloAbsoluto.ComandoAB && mData[5] == ProtocoloAbsoluto.AB05;
    }

    public boolean isAB14(){
        return  mData[0] == ProtocoloAbsoluto.ComandoAB && mData[5] == ProtocoloAbsoluto.AB14;
    }

    public boolean isAbnt73() {
        return mData[0] == ProtocoloAbsoluto.AbntAlteracaoIntervaloMM;
    }
    public boolean isAbnt87() {
        return mData[0] == ProtocoloAbsoluto.AbntLeituraCodigoInstalacao;
    }
    public boolean isLeituraConfiguracaoMedidor() {
        return (mData[0] == ProtocoloAbsoluto.ComandoAB || mData[0] == ProtocoloAbsoluto.ComandoEB)
                && mData[5] == ProtocoloAbsoluto.ConfiguracoesMedidorHospedeiro;
    }

    public boolean isTensaoReferencia() {
        return mData[0] == ProtocoloAbsoluto.AbntComandoEstendido
                && mData[5] == ProtocoloAbsoluto.AbntTensaoNominal;
    }

    public boolean isLeituraGenerica() {
        return mData[0] == ProtocoloAbsoluto.AbntLeituraGenerica;
    }

    public boolean isLeituraParametrosHospedeiro() {
        return mData[0] == ProtocoloAbsoluto.ComandoAB
                && mData[5] == ProtocoloAbsoluto.ParametrosMedidorHospedeiro;
    }

    public boolean existePacotesLeituraParametrosHospedeiroAReceber() {
        return isLeituraParametrosHospedeiro() && mData[8] < 0x06;
    }

    public List<MedidorAbsoluto> listaMedidores() {
        if (ehRespostaSimples || !isLeituraParametrosHospedeiro())
            return null;
        List<MedidorAbsoluto> medidores = new ArrayList<>();
        byte[] pacote = mRespostaComposta.get(0);
        String numeroMedidorHospedeiro = String.format("%02X%02X%02X%02X", pacote[1], pacote[2], pacote[3], pacote[4]);
        for (int i = 0; i < 35; i++) {
            if ((pacote[9 + i / 8] & (0x01 << (i % 8))) != 0) {
                MedidorAbsoluto absoluto = new MedidorAbsoluto();
                long v = 0xff & pacote[17 + 4 * i];
                long numeroUnidadeConsumidora = v;
                v = 0xff & pacote[18 + 4 * i];
                numeroUnidadeConsumidora += (v << 8);
                v = 0xff & pacote[19 + 4 * i];
                numeroUnidadeConsumidora += (v << 16);
                v = 0xff & pacote[20 + 4 * i];
                numeroUnidadeConsumidora += (v << 24);
                absoluto.unidadeConsumidora = new DecimalFormat("000000000").format(numeroUnidadeConsumidora);
                absoluto.numero = numeroMedidorHospedeiro + ProtocoloAbsoluto.NumeroMedidorPeloNumeroSequencial(i);
                absoluto.fases = ProtocoloAbsoluto.NumeroDeFasesPeloNumeroSequencial(i);
                absoluto.status = 0x99;
                absoluto.tipo = i;
                absoluto.numMedidor = Integer.valueOf(absoluto.numero.substring(6));
                medidores.add(absoluto);
            }
        }

        //Collections.sort(medidores, new Comparator<MedidorAbsoluto>() {
        //    public int compare(MedidorAbsoluto m1, MedidorAbsoluto m2) {
        //        return (int) (m1.numMedidor - m2.numMedidor);
        //    }
        //});

        return medidores;
    }

    public byte[] getData() {
        return mData;
    }

    @Override
    public String toString() {
        if (ehRespostaSimples) {
            return ByteArrayToString(mData);
        } else {
            return ByteArrayListToString(mRespostaComposta);
        }
    }

    private String ByteArrayListToString(ArrayList<byte[]> byteArray) {
        StringBuilder res = new StringBuilder();
        for (byte[] data : byteArray) {
            res.append(ByteArrayToString(data));
        }
        return res.toString();
    }

    private String ByteArrayToString(byte[] data) {
        final StringBuilder stringBuilder = new StringBuilder(data.length);
        for (byte byteChar : data)
            stringBuilder.append(String.format("%02X ", byteChar));
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    public class LeituraStatusMedidor {
        public int EstadoUnidadeConsumidora;
    }

    public class LeituraDados51 {
        public long numeroPacotes;
        public long pacoteAtual;
        public long numeroPalavras;
        public long totalGrandezas;
        public int periodoIntegracao;
        public String textUltimoPeriodo;
        public String textUltimaFatura;
        public Calendar ultimoPeriodo;
        public long numeradorCanal1;
        public long denominadorCanal1;
        public long numeradorCanal2;
        public long denominadorCanal2;
        public long numeradorCanal3;
        public long denominadorCanal3;
    }

    public class LeituraDados52 {
        public boolean ultimo;
        public boolean numeroGrandezas;
        public ArrayList<Long> mGrandezasC1 = new ArrayList<Long>();
        public ArrayList<Long> mGrandezasC2 = new ArrayList<Long>();
        public ArrayList<Long> mGrandezasC3 = new ArrayList<Long>();
    }

    public static class LeituraAB03{
        public boolean isLeitura;
        public byte StatusParametrizacao;
        public byte StatusAtivacao;
        public int UC;
        public long IU;
        public long IUTC;
        public byte Sequenciador;
        public byte CodigoErro;
        public byte SubCodigoErro;

        public String toString(){return "AB03 {" +
                "\n?? leitura: " + isLeitura +
                "\n StatusParametrizacao" + StatusParametrizacao +
                "\n StatusAtivacao" + StatusAtivacao +
                "\n UC" + UC +
                "\n IU" + IU +
                "\n IUTC" + IUTC +
                "\n Sequenciador" + Sequenciador +
                "\n CodigoErro" + CodigoErro +
                "\n SubCodigoErro" + SubCodigoErro +
                "\n";}
    }

    public static class LeituraAB04{
        public boolean isLeitura;
        public byte TipoComando;
        public byte[] CodMostrador = new byte[9];
        public byte CodigoErro;
        public byte SubCodigoErro;

        public String toString(){return "AB03 {" +
                "\n?? leitura: " + TipoComando +
                "\n CodMostradores " + CodMostrador[0] + CodMostrador[1] + CodMostrador[2] + CodMostrador[3] + CodMostrador[4] + CodMostrador[5] + CodMostrador[6] + CodMostrador[7] + CodMostrador[8] +
                "\n CodigoErro" + CodigoErro +
                "\n SubCodigoErro" + SubCodigoErro +
                "\n";}
    }

    public static class LeituraAB05{
        public boolean isLeitura;
        public byte ModoEspecial;
        public byte Calibracao;
        public boolean test = false;

        public String toString(){return "AB05 {" +
                "\n?? leitura: " + isLeitura +
                "\n Modo Especial" + ModoEspecial +
                "\n Modo de calibra????o" + Calibracao +
                "\n";}
        }

    public static class LeituraEB17 {

        public boolean isLeitura;
        public byte ValeAlteracao;
        public byte ValeAlteracao2;

        public String TelefonesDeteccaoFalhas;
        public String Telefone1;
        public String Telefone2;
        public String Telefone3;
        public String Telefone4;
        public byte QtdTelefones;
        public byte Eventos;
        public byte Ciclos;
        public byte RepeticoesEtapa1;
        public int Intervalo1;
        public int Intervalo2;
        public String TelefoneKeepAlive;
        public byte Validade;
        public byte FrequenciaKeepAlive;
        public byte ModoPrateleira;

        @Override
        public String toString() {
            return "LeituraEB90 {" +
                    "\n?? leitura: " + isLeitura +
                    "\nAltera????es byte 1: " + ValeAlteracao +
                    "\nAltera????es byte 2: " + ValeAlteracao2 +
                    "\nTelefones Detec????o Falhas: " + TelefonesDeteccaoFalhas +
                    "\nTelefones Keep Alive: " + TelefoneKeepAlive +
                    "\n}";
        }
    }

    public static class LeituraEB90 {

        public boolean isLeitura;
        public int transformadorLido;

        public int potenciaNominal; // S
        public boolean temPapeltermoestabilizado; // P
        public double valorTemperaturaNivel1; // T1
        public double valorTemperaturaNivel2; // T2
        public double expoenteDoOleo; // x
        public double expoenteDoEnrolamento; // y
        public double constanteDeTempoDoOleo; // to
        public double constanteDeTempoDoEnrolamento; // tw
        public double constanteDoModeloTermico11; // K11
        public double constanteDoModeloTermico21; // K21
        public double constanteDoModeloTermico22; // K22
        public double constanteDeDiscretizacao; // Dt
        public double relacaoDePerdasDeCargaNaCorrenteNominalParaPerdasEmVazio; // R
        public double temperaturaDoPontoMaisQuenteNominal; // ??n
        public double elevacaoDeTemperaturaDoTopoDoOleo; // ????or
        public double gradienteObtidoPelaDiferencaEntreTemperaturaDoPontoMaisQuenteEDoTopoDoOleo; // ????hr
        public double nivel1DeAlarmeParaSobretemperaturaDoPontoMaisQuenteDoTransformador; // ??n1
        public double nivel2DeAlarmeParaSobretemperaturaDoPontoMaisQuenteDoTransformador; // ??n2
        public double nivelSobrecorrenteFaseA; // 51A
        public double nivelSobrecorrenteFaseB; // 51B
        public double nivelSobrecorrenteFaseC; // 51C
        public int nivelSobretensaoFaseA; // 59A
        public int nivelSobretensaoFaseB; // 59B
        public int nivelSobretensaoFaseC; // 59C
        public int nivelSubtensaoFaseA; // 27A
        public int nivelSubtensaoFaseB; // 27B
        public int nivelSubtensaoFaseC; // 27C


        @Override
        public String toString() {
            return "LeituraEB90 {" +
                    "\n?? leitura: " + isLeitura +
                    "\nTransformador Lido: " + transformadorLido +
                    "\nPotencia Nominal: " + potenciaNominal +
                    "\nTem Papel Termoestabilizado: " + temPapeltermoestabilizado +
                    "\n}";
        }
    }

    public class LeituraCabecalhoMMSMBloco1 {
        public long numeroPacotes;
        public long pacoteAtual;
        public int numeroBytes;
        public int posicaoAtual;

        public long IU;
        public byte tipoEstrutura;
        public byte tamanhoEstrutura;
        public int kWh;
        public int kV;
        public int kA;
        public int kTHD;
        public int kpWh;
        public int kVp;
        public int kAp;
    }

    public class LeituraCabecalhoMMSM {
        public int DataHora;
        public int[] RegEnergia = new int[4];
        public short[][] Grandezas = new short[2][3];
        public byte flags;
        public short crc;
    }

    public class LeituraCabecalhoQEE {
        public long numeroPacotes;
        public long pacoteAtual;
        public int numeroBytes;
        public int posicaoAtual;
        public long numeroRegistros;
        public long numeroRegistrosValidos;
        public long intevaloQEE;
        public int grandezas;
        public String textdataInicio;
        public Calendar dataInicio;
        public String textdataFim;
        public Calendar dataFim;


        public float DRP;
        public float DRC;
        public float DTT95;
        public float FD95;
        public float tensaoReferencia;
        public float constanteMultiplicacaoFrequencia;
        public float constanteMultiplicacaoTemperatura;
        public float constanteMultiplicacaoTensao;
        public float constanteMultiplicacaoDesequilibrio;
        public float constanteMultiplicacaoHarmonicas;
        public float constanteMultiplicacaoFatorPotencia;
        public float percentualTensaoPrecariaSuperior;
        public float percentualTensaoPrecariaInferior;
        public float percentualTensaoCriticaSuperior;
        public float percentualTensaoCriticaInferior;
        public byte tipoLigacao;
    }

    public class LeituraQEE {
        public ArrayList<DadosQEE> mDadosQEE = new ArrayList<DadosQEE>();

        public void preencheDados(byte[] data) {
            DadosQEE dadosQEE = new DadosQEE();
            dadosQEE.status = data[0];
            dadosQEE.VTCDMomentaneos = data[1];
            dadosQEE.VTCDTemporarios = data[2];
            dadosQEE.variacoesFrequencia = data[3];
            dadosQEE.interrupcoes = data[4];
            dadosQEE.frequencia = ByteArrayToLong(data, 5);
            dadosQEE.temperatura = ByteArrayToLong(data, 7);
            dadosQEE.tensaoA = ByteArrayToLong(data, 9);
            dadosQEE.tensaoB = ByteArrayToLong(data, 11);
            dadosQEE.tensaoC = ByteArrayToLong(data, 13);
            dadosQEE.desequilibrio = ByteArrayToLong(data, 15);
            dadosQEE.DHTA = ByteArrayToLong(data, 17);
            dadosQEE.DHTB = ByteArrayToLong(data, 19);
            dadosQEE.DHTC = ByteArrayToLong(data, 21);
            dadosQEE.fatPotenciaA = ByteArrayToLong(data, 23);
            dadosQEE.fatPotenciaB = ByteArrayToLong(data, 25);
            dadosQEE.fatPotenciaC = ByteArrayToLong(data, 27);
            dadosQEE.fatPotenciaTrifasico = ByteArrayToLong(data, 29);
            mDadosQEE.add(dadosQEE);
        }
    }

    public class LeituraQEESM2 {
        public ArrayList<DadosQEESM2> mDadosQEESM2 = new ArrayList<DadosQEESM2>();

        public void preencheDados(byte[] data) {
            DadosQEESM2 dadosQEESM2 = new DadosQEESM2();
            dadosQEESM2.status = data[0];
            dadosQEESM2.VTCDMomentaneos = data[1];
            dadosQEESM2.VTCDTemporarios = data[2];
            dadosQEESM2.variacoesFrequencia = data[3];
            dadosQEESM2.interrupcoes = data[4];
            dadosQEESM2.frequencia = ByteArrayToLong(data, 5);
            dadosQEESM2.temperatura = ByteArrayToLong(data, 7);
            dadosQEESM2.tensaoA = ByteArrayToLong(data, 9);
            dadosQEESM2.tensaoB = ByteArrayToLong(data, 11);
            dadosQEESM2.tensaoC = ByteArrayToLong(data, 13);
            dadosQEESM2.desequilibrio = ByteArrayToLong(data, 15);
            dadosQEESM2.DHTA = ByteArrayToLong(data, 17);
            dadosQEESM2.DHTB = ByteArrayToLong(data, 19);
            dadosQEESM2.DHTC = ByteArrayToLong(data, 21);
            mDadosQEESM2.add(dadosQEESM2);
        }
    }

    public class DadosQEE {
        public long status;
        public long VTCDMomentaneos;
        public long VTCDTemporarios;
        public long variacoesFrequencia;
        public long interrupcoes;
        public long frequencia;
        public long temperatura;
        public long tensaoA;
        public long tensaoB;
        public long tensaoC;
        public long desequilibrio;
        public long DHTA;
        public long DHTB;
        public long DHTC;
        public long fatPotenciaA;
        public long fatPotenciaB;
        public long fatPotenciaC;
        public long fatPotenciaTrifasico;

    }

    public class DadosQEESM2 {
        public long status;
        public long VTCDMomentaneos;
        public long VTCDTemporarios;
        public long variacoesFrequencia;
        public long interrupcoes;
        public long frequencia;
        public long temperatura;
        public long tensaoA;
        public long tensaoB;
        public long tensaoC;
        public long desequilibrio;
        public long DHTA;
        public long DHTB;
        public long DHTC;

    }

    public class GrandezaMM {
        public long grandeza;
        public long valor;
    }
}
