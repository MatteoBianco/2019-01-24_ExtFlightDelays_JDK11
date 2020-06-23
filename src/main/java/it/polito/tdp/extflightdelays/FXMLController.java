package it.polito.tdp.extflightdelays;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.extflightdelays.model.Model;
import it.polito.tdp.extflightdelays.model.StatePair;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea txtResult;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private ComboBox<String> cmbBoxStati;

    @FXML
    private Button btnVisualizzaVelivoli;

    @FXML
    private TextField txtT;

    @FXML
    private TextField txtG;

    @FXML
    private Button btnSimula;

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	this.model.createGraph();
    	this.cmbBoxStati.getItems().clear();
    	this.cmbBoxStati.getItems().addAll(this.model.getListStates());
    	this.btnVisualizzaVelivoli.setDisable(false);
    	this.btnSimula.setDisable(false);
    	txtResult.setText("Grafo creato!\n");
    }

    @FXML
    void doSimula(ActionEvent event) {
    	txtResult.clear();
    	Integer T;
    	Integer G;
    	try {
    		T = Integer.parseInt(txtT.getText());
    		G = Integer.parseInt(txtG.getText());
    	} catch(NumberFormatException e) {
    		txtResult.setText("Inserire 2 numeri interi per indicare il numero di turisti (T) e il "
    				+ "numero di giorni (G) per effettuare la simulazione!\n");
    		return;
    	}
    	if(T <= 0 || G <= 0) {
    		txtResult.setText("Inserire 2 numeri positivi per indicare il numero di turisti (T) e il "
    				+ "numero di giorni (G) per effettuare la simulazione!\n");
    		return;
    	}
    	String state = this.cmbBoxStati.getValue();
    	if(state == null) {
    		txtResult.setText("Errore: selezionare uno stato dall'apposita tendina per "
    				+ "procedere con la simulazione!\n");
    		return;
    	}
    	this.model.simulate(T, G, state);
    	
    	Map<String, Integer> touristsInEachState = this.model.turistsInEachState();
    	txtResult.appendText("RISULTATO SIMULAZIONE:\n\n");
    	for(String s : touristsInEachState.keySet()) {
    		txtResult.appendText("Stato: " + s + " --- Numero turisti: " + touristsInEachState.get(s) + "\n");
    	}

    }

    @FXML
    void doVisualizzaVelivoli(ActionEvent event) {
    	txtResult.clear();
    	String state = this.cmbBoxStati.getValue();
    	if(state == null) {
    		txtResult.setText("Errore: selezionare uno stato dall'apposita tendina per trovare le destinazioni disponibili!\n");
    		return;
    	}
    	List<StatePair> destinations = this.model.getAllDestinations(state);
    	if(destinations.isEmpty()) {
    		txtResult.setText("Nessuna destinazione trovata dall'aeroporto selezionato.\n");
    		return;
    	}
    	txtResult.appendText("Destinazioni trovate: \n\n");
    	Integer count = 0;
    	for(StatePair sp : destinations) {
    		txtResult.appendText(sp.getDestination() + " - numero diversi velivoli: " + sp.getWeight() + "; \t ");
    		count ++;
    		if(count == 2) {
    			count = 0;
    			txtResult.appendText("\n");
    		}
    	}
    }
    
    @FXML
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert cmbBoxStati != null : "fx:id=\"cmbBoxStati\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnVisualizzaVelivoli != null : "fx:id=\"btnVisualizzaVelivoli\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert txtT != null : "fx:id=\"txtT\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert txtG != null : "fx:id=\"txtG\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		this.btnSimula.setDisable(true);
		this.btnVisualizzaVelivoli.setDisable(true);
	}
}
