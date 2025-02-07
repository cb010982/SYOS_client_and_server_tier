/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syos.system.service;

import com.mycompany.syos.system.designpatterns.reports.BillReport;
import com.mycompany.syos.system.designpatterns.reports.EndOfDaySalesReport;
import com.mycompany.syos.system.designpatterns.reports.ReorderReport;
import com.mycompany.syos.system.designpatterns.reports.ReportContext;
import com.mycompany.syos.system.designpatterns.reports.ReshelveReport;
import com.mycompany.syos.system.designpatterns.reports.StockReport;
/**
 *
 * @author User
 */
public class ReportService {
    
    private ReportContext reportContext;

    public ReportService() {
        this.reportContext = new ReportContext();
    }

    public synchronized void generateEndOfDaySalesReport() {
            reportContext.setReportStrategy(new EndOfDaySalesReport());
            reportContext.displayReport();
        }

    public synchronized void generateReshelveReport() {
            reportContext.setReportStrategy(new ReshelveReport());
            reportContext.displayReport();
    }

    public synchronized void generateReorderReport() {
            reportContext.setReportStrategy(new ReorderReport());
            reportContext.displayReport();
    }

    public synchronized void generateStockReport() {
        reportContext.setReportStrategy(new StockReport());
        reportContext.displayReport();
    }

    public synchronized void generateBillReport() {
        reportContext.setReportStrategy(new BillReport());
        reportContext.displayReport();
    }
}
