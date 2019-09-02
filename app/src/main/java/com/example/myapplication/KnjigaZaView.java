package com.example.myapplication;


public class KnjigaZaView
{

        public String klijent;
        public int godina;
        public String opis;
        public int id_knjiga;

        public String getKlijent()
        {
            return klijent;
        }

        public int getGodina()
        {
            return godina;
        }

        public String getOpis()
        {
            return opis;
        }

        public int getId_knjiga() { return id_knjiga; }

    public KnjigaZaView(int id_knjiga, String klijent, int godina, String opis)
    {
            this.id_knjiga = id_knjiga;
            this.klijent = klijent;
            this.godina = godina;
            this.opis = opis;
    }

}
