package com.an.videoplayer.utils;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectUtils<T> {
    public ObjectUtils() {
    }

    public void writeObject(T object, String filename) {
        ObjectOutputStream out = null;

        try {
            out = new ObjectOutputStream(new FileOutputStream(filename));
            out.writeObject(object);
        } catch (Exception var13) {
            var13.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException var12) {
                    var12.printStackTrace();
                }
            }

        }

    }

    public T readObject(String fileName) {
        File f = new File(fileName);

        if (f.exists()) {
            ObjectInputStream in = null;

            try {
                in = new ObjectInputStream(new FileInputStream(fileName));
                return (T) in.readObject();
            }catch (EOFException e) {
                return null;
//                throw new RuntimeException(e);
            } catch (Exception e) {
                return null;
//                throw new RuntimeException(e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                    }
                }
            }
        } else {
            return null;
        }
    }
}