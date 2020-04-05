package com.codeitforyou.vouchers.manager;

import com.codeitforyou.vouchers.api.Voucher;

import java.util.List;

public class VoucherManager {
    private List<Voucher> vouchers;

    public VoucherManager(List<Voucher> vouchers) {
        this.vouchers = vouchers;
    }

    public List<Voucher> getVouchers() {
        return vouchers;
    }

    public Voucher getVoucher(String id) {
        return vouchers.stream().filter(voucher -> voucher.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
    }
}
