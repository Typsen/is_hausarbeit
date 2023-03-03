package brainball;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.LinkedList;
import java.util.List;

public class BrainBallController {

    private BrainBall displaylBrainBall;
    private BrainBall previewBrainBall;

    public Pane pane_BrainBall;

    private List<Label> labels = new LinkedList<>();
    private List<Rectangle> rectangles = new LinkedList<>();

    public Slider sdr_Slider;

    public Button btn_Flip;
    public Button btn_Undo;
    public Button btn_Redo;

    public Button btn_Shuffle;
    public Button btn_Random;
    public Button btn_Solve;
    public Button btn_Reset;

    public ListView<String> lst_moves;

    @FXML
    private void initialize() {
        this.displaylBrainBall = new BrainBall();
        pane_BrainBall.getChildren()
                .filtered(node -> node instanceof Label || node instanceof Rectangle)
                .forEach(node -> {
                    if (node instanceof Label) {
                        labels.add((Label) node);
                    } else {
                        rectangles.add((Rectangle) node);
                    }
                });
        updateLables(displaylBrainBall);

        // Makes the slider tick in discrete steps
        sdr_Slider.valueProperty().addListener((observable, oldValue, newValue) -> sdr_Slider.setValue(newValue.intValue()));

        // Displays a preview of the next rotation
        sdr_Slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(Math.abs(newValue.intValue()-oldValue.intValue())==1){
                if(previewBrainBall == null){ previewBrainBall = new BrainBall(displaylBrainBall);}
                int move = newValue.intValue()-oldValue.intValue();
                previewBrainBall.makeMove(move);
                updateLables(previewBrainBall);
            }
        });

        /* On Klick: -
         * On Release: performs a rotation based on the current slider position.
         * */
        sdr_Slider.valueChangingProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                displaylBrainBall.makeMove((int) Math.round(sdr_Slider.getValue()));
                sdr_Slider.setValue(0);
                updateLables(displaylBrainBall);
                updateMoves(displaylBrainBall);
                previewBrainBall = null;
            }
        });

        btn_Flip.setOnMouseEntered(event -> {
            previewBrainBall = new BrainBall(displaylBrainBall);
            previewBrainBall.flip();
            updateLables(previewBrainBall);
        });

        btn_Flip.setOnMouseExited(event -> {
            updateLables(displaylBrainBall);
            previewBrainBall=null;
        });
    }

    private void updateLables(BrainBall brainBall) {
        for (int i = 0; i < brainBall.getRing().size(); i++) {
            BrainBall.RingElement currentRingElement = brainBall.getRing().get(i);
            Color currentColor = currentRingElement.getColor() == BrainBall.RingElement.Color.WHITE ? Color.WHITE : Color.YELLOW;
            rectangles.get(i).setFill(currentColor);
            labels.get(i).setText(Integer.toString(currentRingElement.getNumber()+1));
        }
    }

    @FXML
    public void updateMoves(BrainBall brainBall){
        List<String> moveStrings = new LinkedList<>();
        brainBall.getMoves().forEach(move -> {
            int ringSize = brainBall.getRing().size();
            if (-ringSize < move && move < ringSize) {
                moveStrings.add("Rotate:\t"+move);
            } else if (move == -ringSize || move == ringSize) {
                moveStrings.add("Flip:\t\t"+move);
            }
        });
        ObservableList<String> moves = FXCollections.observableList(moveStrings);
        lst_moves.setItems(moves);
        lst_moves.scrollTo(lst_moves.getItems().size()-1);
    }

    @FXML
    public void btn_Flip_OnAction() {
        displaylBrainBall.flip();
        updateLables(displaylBrainBall);
        updateMoves(displaylBrainBall);
    }

    @FXML
    public void btn_Undo_OnAction() {
        displaylBrainBall.undo();
        updateLables(displaylBrainBall);
        updateMoves(displaylBrainBall);
    }

    @FXML
    public void btn_Redo_OnAction() {
        displaylBrainBall.redo();
        updateLables(displaylBrainBall);
        updateMoves(displaylBrainBall);
    }

    @FXML
    public void btn_Shuffle_OnAction() {
        displaylBrainBall.shuffle();
        updateLables(displaylBrainBall);
        updateMoves(displaylBrainBall);
    }

    @FXML
    public void btn_Random_OnAction() {
        displaylBrainBall.randomMove();
        updateLables(displaylBrainBall);
        updateMoves(displaylBrainBall);
    }

    @FXML
    public void btn_Solve_OnAction() {
        displaylBrainBall.solve();
        updateLables(displaylBrainBall);
        updateMoves(displaylBrainBall);
    }

    @FXML
    public void btn_Reset_OnAction() {
        this.displaylBrainBall = new BrainBall();
        updateLables(displaylBrainBall);
        updateMoves(displaylBrainBall);
    }
}