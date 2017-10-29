package controller;

import model.Model;
import view.View;

public class Controller {
    private Model model;
    private View view;


    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        this.link(model,view);
    }

    public void link(Model model, View view) {
        //Setting the Library of the View to be identical with the library in the Model
        view.setLibrary(model.getLibrary());

    }
}
