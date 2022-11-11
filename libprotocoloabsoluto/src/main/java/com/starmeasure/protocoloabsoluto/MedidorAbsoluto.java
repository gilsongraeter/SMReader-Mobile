package com.starmeasure.protocoloabsoluto;

import java.io.Serializable;

public class MedidorAbsoluto implements Serializable {
    public int tipo;
    public String unidadeConsumidora;
    public String numero;
    public long numMedidor;
    public int fases;
    public int status;
    public boolean semRele;

    @Override
    public String toString() {
        return "MedidorAbsoluto{" +
                "unidadeConsumidora='" + unidadeConsumidora + '\'' +
                ", numero='" + numero + '\'' +
                ", numMedidor=" + numMedidor +
                ", fases=" + fases +
                ", status=" + status +
                ", semRele=" + semRele +
                ", tipo=" + tipo +
                '}';
    }
}
