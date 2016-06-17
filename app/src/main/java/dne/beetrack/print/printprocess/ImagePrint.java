/**
 * ImagePrint for printing
 *
 * @author Brother Industries, Ltd.
 * @version 2.2
 */
package dne.beetrack.print.printprocess;

import android.content.Context;

import com.brother.ptouch.sdk.PrinterInfo.ErrorCode;

import java.util.ArrayList;

import dne.beetrack.print.common.MsgDialog;
import dne.beetrack.print.common.MsgHandle;

public class ImagePrint extends BasePrint {

    protected ArrayList<String> mImageFiles;

    public ImagePrint(Context context, MsgHandle mHandle, MsgDialog mDialog) {
        super(context, mHandle, mDialog);
    }

    /**
     * set print data
     */
    public void setFiles(ArrayList<String> files) {
        mImageFiles = files;
    }

    /**
     * do the particular print
     */
    @Override
    protected void doPrint() {

        int count = mImageFiles.size();

        for (int i = 0; i < count; i++) {

            String strFile = mImageFiles.get(i);

            mPrintResult = mPrinter.printFile(strFile);

            // if error, stop print next files
            if (mPrintResult.errorCode != ErrorCode.ERROR_NONE) {
                break;
            }
        }
    }
}