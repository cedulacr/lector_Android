# LectorCedulasCR
Proyecto de Android Studio de app para lectura de cedulas de identifican de Costa Rica.

El codigo de barras de las Cedulas Costaricenses son de tipo PDF417(https://en.wikipedia.org/wiki/PDF417), y se encuentran codificadas por un algoritmo XOR(https://en.wikipedia.org/wiki/XOR_cipher), este tipo de codificacion tiene una llave, en este caso es de 17 entradas pero puede variar.

Este proyecto esta basado en el ejemplo https://github.com/PDF417/pdf417-android/tree/master/Pdf417MobiDemo/pdf417MobiDemo de igual forma el decodificador debe funcionar con cualquier tipo de lector de barras PDF417.
para crear tu propio proyecto sigue las instrucciones de aca: https://github.com/PDF417/pdf417-android#quickIntegration

En caso de algun error no duden en reportarlo, pronto esta subiendo un repositorio para el framework IONICv2, esto para que sea multiplataforma, ademas de agregar documentacion interna a este repositorio.



#Codigo
El archivo encargado de la decodificacion del codigo se llama CedulaCR, este es una Classe de java que se conforma por la llave anteriormente mensionada y una funcion llamada 'parse' que efectua la decodificacion, ademas esta optiene los trosos de informacion y lo devulve como una clase dummy para un manejo mas sensillo de la informacion optenida.

Codigo de la clase CedulaCR

```code
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

  ```
  
  <a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
  
