package core.main;

import java.io.*;
import java.util.HashMap;

import core.file.Index;
import core.file.Management;

public class Main {

    private static final String DATA_INSTRUCTOR = "instructor.dat";
    private static final String DATA_APPRENTICE = "apprentice.dat";
    private static final String DATA_SESSION = "session.dat";

    private static final String FILE_INSTRUCTOR = "index_INS.idx";
    private static final String FILE_APPRENTICE = "index_APP.idx";
    private static final String FILE_SESSION = "index_SES.idx";

    static HashMap<Integer, Long> INDEX_INSTRUCTOR = new HashMap<>();
    static HashMap<Integer, Long> INDEX_APPRENTICE = new HashMap<>();
    static HashMap<Integer, Long> INDEX_SESSION = new HashMap<>();

    public static void main(String[] args) {
        
        Index index = new Index();

        index.loadIndex(FILE_INSTRUCTOR, INDEX_INSTRUCTOR);
        index.loadIndex(FILE_APPRENTICE, INDEX_APPRENTICE);
        index.loadIndex(FILE_SESSION, INDEX_SESSION);

        System.out.println("YAY");

        String name;
        int id;
        long phone;
        short numberOfSpecialties, numberOfSessions;

        
        try {
            RandomAccessFile apprenticeRAF = new RandomAccessFile(DATA_APPRENTICE, "rw");
            FileReader outFile = new FileReader("datosAprenEjemplo" + ".txt");
            BufferedReader Buffer_Lectura = new BufferedReader(outFile);
            String line = null;
            line = Buffer_Lectura.readLine();
            String temp[] = line.split("\t");
            name = temp[0];
            name = name + " ".repeat(50 - name.length());
            id = Integer.parseInt(temp[1]);
            numberOfSpecialties = Short.parseShort(temp[2]);
            phone = Long.parseLong(temp[3]);
            numberOfSessions = Short.parseShort(temp[4]);

            apprenticeRAF.writeChars(name);
            apprenticeRAF.writeInt(id);
            apprenticeRAF.writeShort(numberOfSpecialties);
            apprenticeRAF.writeLong(phone);
            apprenticeRAF.writeShort(numberOfSessions);
        } catch (IOException e) {
            e.printStackTrace();
        }
    
    }
}