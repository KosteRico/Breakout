package com.kosterico.game_elements;

import com.badlogic.gdx.Gdx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SavingData {

    private static Map<Date, Integer> dates;

    private static final byte MAX_CAPACITY = 7;

    public static void loadData() {
        if (dates == null) {
            try {
                File results = new File("C:\\Users\\KosteRico\\Desktop\\JAVA_GAME\\MFC\\android\\assets\\results.ser");
                FileInputStream fis = new FileInputStream(results);
                BufferedReader reader = new BufferedReader(new FileReader(results));
                if (reader.readLine() != null) {
                    ObjectInputStream input = new ObjectInputStream(fis);
                    Map<Date, Integer> map = (HashMap<Date, Integer>) input.readObject();
                    dates = map;
                    input.close();
                    fis.close();
                } else {
                    dates = new HashMap<Date, Integer>();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    public static void saveDatas(Date d, int i) {

        if (dates.size() < MAX_CAPACITY) {
            dates.put(d, i);
        } else {
            dates.clear();
            dates.put(d, i);
        }

        try {
            File results = new File("C:\\Users\\KosteRico\\Desktop\\JAVA_GAME\\MFC\\android\\assets\\results.ser");
            FileOutputStream fos = new FileOutputStream(results, false);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            if (dates != null)
                oos.writeObject(dates);

            oos.close();
            fos.close();
        } catch (IOException e) {

        }
    }

    public static Map<Date, Integer> getDates() {
        return dates;
    }

}
