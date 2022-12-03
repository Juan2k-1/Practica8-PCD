/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import Controlador.Tienda;
import Vista.CanvasTienda;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juald
 */
public class Comprar implements Callable<Integer> {

    private int id;
    private CanvasTienda cv;
    private Tienda tienda;

    public Comprar(int id, CanvasTienda cv, Tienda tienda) {
        this.id = id;
        this.cv = cv;
        this.tienda = tienda;
    }

    @Override
    public Integer call() {

        try {
            System.out.println("Hilo compra: " + Thread.currentThread().getId() + " entra en la cola de espera de compra la tarea: " + id + " para comprar");
            this.cv.inserta('C', this.id);
            char tipo = this.tienda.EntraComprar(this.id);
            //System.out.println("Hilo compra: " + Thread.currentThread().getId() + " entra en la tienda la tarea: " + id + " para comprar");
            Thread.sleep(10000);
            this.tienda.SaleComprar(tipo, this.id);
            //System.out.println("Hilo compra: " + Thread.currentThread().getId() + " ya he comprado, salgo de la tienda");
        } catch (InterruptedException ex) {
            Logger.getLogger(Comprar.class.getName()).log(Level.SEVERE, null, ex);
        }

        return id;
    }
}
