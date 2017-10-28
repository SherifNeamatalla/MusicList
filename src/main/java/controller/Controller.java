package controller;

import model.Model;

public class Controller {
    private Model model;
    private View view;
    public Controller(Model model, View view)
    {
        this.model = model;
        this.view = view;
        this.link(model,view);
    }

    private void link(Model model, View view)
    {

    }
}
