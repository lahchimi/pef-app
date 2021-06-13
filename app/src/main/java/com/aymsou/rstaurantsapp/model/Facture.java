package com.aymsou.rstaurantsapp.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Facture {

    public List<FactureItem> items = new ArrayList();
    public String Fullname, PhoneNumber;

    @Override
    public String toString() {
        String resstr="";
        resstr+="<h3>Nom et prénom : "+this.Fullname+"</h3>";
        resstr+="<h3>Numéro de téléphone : "+this.PhoneNumber+"</h3>";
        resstr+="<table border=1><tr><td>élément</td><td>Quanité</td><td>Prix</td></tr>";
        for(FactureItem item : this.items){
            resstr+="<tr><td>"+item.itemName+"</td><td>"+item.quantity+"</td><td>"+(item.quantity*item.price)+" MAD</td></tr>";
        }
        resstr+="</table>";
        resstr+="<h3>Totale : "+this.calculateTotal()+" MAD</h3>";
       return resstr;
    }

    public float calculateTotal(){
        float totalPrice = 0;
        for(FactureItem item : this.items){
            totalPrice += item.price * item.quantity;
        }
        return totalPrice;
    }

}
