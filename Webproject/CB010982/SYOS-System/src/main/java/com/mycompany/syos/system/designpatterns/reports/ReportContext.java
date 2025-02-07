/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syos.system.designpatterns.reports;

/**
 *
 * @author User
 */
public class ReportContext {

    private ReportStrategy reportStrategy;

    public void setReportStrategy(ReportStrategy reportStrategy) {
        this.reportStrategy = reportStrategy;
    }

    public void displayReport() {
        if (reportStrategy != null) {
            reportStrategy.generateReport();
        } else {
            System.out.println("No report strategy selected. Please select a valid report strategy.");
        }
    }
}
