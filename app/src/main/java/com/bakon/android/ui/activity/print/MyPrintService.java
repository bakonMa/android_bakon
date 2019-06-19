package com.bakon.android.ui.activity.print;

import android.printservice.PrintJob;
import android.printservice.PrintService;
import android.printservice.PrinterDiscoverySession;

public class MyPrintService extends PrintService {
    @Override
    protected PrinterDiscoverySession onCreatePrinterDiscoverySession() {
        return null;
    }

    @Override
    protected void onRequestCancelPrintJob(PrintJob printJob) {

    }

    @Override
    protected void onPrintJobQueued(PrintJob printJob) {

    }
}
