package com.example.manager.boundary;

import java.awt.event.ActionEvent;

public class NhanVienHomeFrm {
    private String btnTraVe;

    public NhanVienHomeFrm() {
    }

    public SearchVeTauFrm moManHinhSearchVeTau() {
        return new SearchVeTauFrm();
    }

    public void actionPerformed(ActionEvent e) {
        if (e != null && "TraVe".equals(e.getActionCommand())) {
            moManHinhSearchVeTau();
        }
    }

    public String getBtnTraVe() {
        return btnTraVe;
    }

    public void setBtnTraVe(String btnTraVe) {
        this.btnTraVe = btnTraVe;
    }
}
