package com.aim.printerdemo_nosdk;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Ildi Xhaferri on 4/2/2018.
 */

public class PrinterInstance {

    OutputStream mmOutputStream;


    PrinterInstance(OutputStream mmOutputStream){
        this.mmOutputStream = mmOutputStream;

    }
    public void setFont(int mCharacterType, int mWidth, int mHeight, int mBold, int mUnderline) throws IOException {

        byte mFontSize = 0;
        byte mFontMode = 0;

        if(mBold != 0 && mBold != 1) {
            mBold = 0;
        }

        mFontMode = (byte)(mFontMode | mBold << 3);
        if(mUnderline != 0 && mUnderline != 1) {
            mUnderline = 0;
        }

        mFontMode = (byte)(mFontMode | mUnderline << 7);
        if(mCharacterType != 0 && mCharacterType != 1) {
            mCharacterType = 0;
        }

        mFontMode = (byte)(mFontMode | mCharacterType << 0);
        mmOutputStream.write(new byte[]{27, 33, mFontMode});
        if(mWidth < 0 || mWidth > 7) {
            mWidth = 0;
        }

        mFontSize = (byte)(mFontSize | mWidth << 4);
        if(mHeight < 0 | mHeight > 7) {
            mHeight = 0;
        }

        mFontSize = (byte)(mFontSize | mHeight);
        mmOutputStream.write(new byte[]{29, 33, mFontSize});
    }

    public void setPrinter(int command, int value) throws IOException {
        byte[] arrayOfByte = new byte[3];
        switch(command) {
            case 0:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 74;
                break;
            case 1:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 100;
                break;
            case 4:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 86;
                break;
            case 11:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 32;
                break;
            case 13:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 97;
                if(value > 2 || value < 0) {
                    value = 0;
                }
        }

        arrayOfByte[2] = (byte)value;
        mmOutputStream.write(arrayOfByte);
    }

}
