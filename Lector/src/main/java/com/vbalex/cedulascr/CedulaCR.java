package com.vbalex.cedulascr;

/**
 * Created by alex on 2/19/17.
 */

public class CedulaCR {
    private static byte[] keysArray = new byte[]{
            (byte)0x27,
            (byte)0x30,
            (byte)0x04,
            (byte)0xA0,
            (byte)0x00,
            (byte)0x0F,
            (byte)0x93,
            (byte)0x12,
            (byte)0xA0,
            (byte)0xD1,
            (byte)0x33,
            (byte)0xE0,
            (byte)0x03,
            (byte)0xD0,
            (byte)0x00,
            (byte)0xDf,
            (byte)0x00
    };

    public static Persona parse(byte[] raw){
        String d= "";
        int j = 0;
        for (int i = 0; i < raw.length; i++) {
            if (j == 17) {
                j = 0;
            }
            char c = (char) (keysArray[j] ^ ((char) (raw[i])));
            if((c+"").matches("^[a-zA-Z0-9]*$")){
                d += c;
            }else{
                d +=' ';
            }
            j ++;
        }
        Persona p = new Persona();
        try {
            p.setCedula(d.substring(0, 9).trim());
            p.setApellido1(d.substring(9, 35).trim());
            p.setApellido2(d.substring(35, 61).trim());
            p.setNombre(d.substring(61, 91).trim());
            p.setGenero(d.charAt(91));
            p.setFechaNacimiento(d.substring(92, 96)+"-"+d.substring(96, 98)+"-"+d.substring(98, 100));
            p.setFechaVencimiento(d.substring(100, 104)+"-"+d.substring(104, 106)+"-"+d.substring(106, 108));
        }catch (Exception e){
            p = null;
        }
        return p;
    }
}
