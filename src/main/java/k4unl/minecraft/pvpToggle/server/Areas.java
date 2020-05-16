package k4unl.minecraft.pvpToggle.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import k4unl.minecraft.pvpToggle.lib.Log;
import k4unl.minecraft.pvpToggle.lib.PvPArea;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class Areas {

    private static List<PvPArea> areaList;

    public static void init() {

        areaList = new ArrayList<PvPArea>();
    }

    public static PvPArea getAreaByName(String areaName) {

        for (PvPArea a : areaList) {
            if (a.getName().equals(areaName)) {
                return a;
            }
        }
        return null;
    }

    public static void removeAreaByName(String areaName){
        for (PvPArea a : areaList) {
            if (a.getName().equals(areaName)) {
                areaList.remove(a);
                return;
            }
        }
    }

    public static List<PvPArea> getAreas(){
        return areaList;
    }

    public static void addToList(PvPArea toAdd){
        areaList.add(toAdd);
    }

    public static void readFromFile(File dir){
        areaList.clear();
        if(dir != null){
            Gson gson = new Gson();
            String p = dir.getAbsolutePath();
            p += "/pvptoggle.areas.json";
            File f = new File(p);
            if(!f.exists()){
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                FileInputStream ipStream = new FileInputStream(f);
                InputStreamReader reader = new InputStreamReader(ipStream);
                BufferedReader bReader = new BufferedReader(reader);
                String json = bReader.readLine();
                reader.close();
                ipStream.close();
                bReader.close();

                Type myTypeMap = new TypeToken<List<PvPArea>>(){}.getType();
                areaList = gson.fromJson(json, myTypeMap);
                if(areaList == null){
                    areaList = new ArrayList<PvPArea>();
                }

                Log.debug("Read from file: " + json);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                Log.debug("Areas loaded from " + p);
            }


        }
    }

    public static void saveToFile(File dir){
        if(dir != null){
            Gson gson = new Gson();
            String json = gson.toJson(areaList);
            Log.debug("Saving: " + json);
            String p = dir.getAbsolutePath();
            p += "/pvptoggle.areas.json";
            File f = new File(p);
            if(!f.exists()){
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                PrintWriter opStream = new PrintWriter(f);
                opStream.write(json);
                opStream.flush();
                opStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }finally {
                Log.debug("Areas saved to " + p);
            }

        }
    }

}
