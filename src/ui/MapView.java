package ui;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import event.EventType;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import map.MapGenerator;

public class MapView extends StackPane{
	
	private static final double W = 1920, H = 1080;
	private static final double MARGIN_X = 400, MARGIN_Y = 100;
	private static final double MIN_ROW_GAP = 150;
	private static final double NODE_SIZE = 72;
	private static final double BOSS_SIZE = 120;
	private static final double LINE_GAP = 6;
	private static final Map<EventType, Image> IMAGES = new EnumMap<>(EventType.class);
	
	private final MapGenerator gen;
	private final double rowGap;
	private final double contentHeight;
	
	public MapView(MapGenerator gen, int currentNode, Set<Integer> visited, Consumer<Integer> onNodeSelected, Runnable onBack) {
		this.gen = gen;
		
		rowGap = Math.max(MIN_ROW_GAP, (H - 2 * MARGIN_Y) / gen.getDepth());
		contentHeight = 2 * MARGIN_Y + gen.getDepth() * rowGap;
		
		Pane map = new Pane();
		map.setPrefSize(W, contentHeight);
		map.setMinSize(W, contentHeight);
		map.setMaxSize(W, contentHeight);
		map.setStyle("-fx-background-color: radial-gradient(center 50% 50%, radius 80%, #e6d3a3, #c9a66b);");		
		int vertices = gen.bossNode() + 1;
		List<Integer> reachable = (currentNode < 0) ? gen.getStartNodes() : gen.getNextNodes(currentNode);
		
		for (int v = 0; v < vertices; v ++) {
			for (int w : gen.getNextNodes(v)) {
				addLine(map, v, w, visited);
			}
		}
		
		for (int v = 0; v < vertices; v++) {
			boolean canClick = reachable.contains(v);
			EventType type = gen.getType(v);
			double size = sizeOf(v);
			
			ImageView icon = new ImageView(imageFor(type));
			icon.setFitWidth(size);
			icon.setFitHeight(size);
			icon.setPreserveRatio(true);
			icon.setSmooth(true);
			
			if (v == currentNode)
				icon.setEffect(new DropShadow(25, Color.web("#3b2a14")));
			else if (canClick)
				icon.setEffect(new DropShadow(25, Color.web("#b3261e")));
			
			StackPane node = new StackPane(icon);
			node.setLayoutX(x(v) - size / 2);
			node.setLayoutY(y(v) - size / 2);
			
			if (canClick) {
				final int vertex = v;
				node.setCursor(Cursor.HAND);
				node.setOnMouseClicked(e -> onNodeSelected.accept(vertex));
				node.setOnMouseEntered(e -> {
					node.setScaleX(1.15); node.setScaleY(1.15);
				});
				node.setOnMouseExited(e -> {
					node.setScaleX(1.0); node.setScaleY(1.0);
				});
			} else if (!visited.contains(v)) {
				node.setOpacity(0.55);
			}
			map.getChildren().add(node);
		}
		
		ScrollPane scroll = new ScrollPane(map);
		scroll.setHbarPolicy(ScrollBarPolicy.NEVER);
		scroll.setVbarPolicy(ScrollBarPolicy.NEVER);
		scroll.setPannable(true);
		scroll.setStyle("-fx-background: #d9bf8c; -fx-background-color: #d9bf8c;");		
		Platform.runLater(() -> {
			double range = contentHeight - H;
			if (range <= 0) {
				scroll.setVvalue(0);
				return;
			}
			double focusY = (currentNode < 0) ? contentHeight : y(currentNode);
			double top = (currentNode < 0) ? range : focusY - 0.75 * H;
			scroll.setVvalue(Math.max(0, Math.min(1, top/range)));
		});
		
		Button back = new Button("Menu");
		back.setStyle("-fx-font-size: 24px; -fx-background-color: #8b5a2b; "
	            + "-fx-text-fill: #f5e6c4; -fx-background-radius: 6;");
		back.setOnAction(e -> onBack.run());
		StackPane.setAlignment(back, Pos.TOP_LEFT);
		StackPane.setMargin(back, new Insets(30));
		getChildren().addAll(scroll, back);
		
	}
	
	private static Image imageFor(EventType type) {
		return IMAGES.computeIfAbsent(type, t -> {
			String path = "/images/" + t.name().toLowerCase() + ".png";
			var url = MapView.class.getResource(path);
			if (url == null) {
				System.err.println("Missing image: " + path);
				return null;
			}
			return new Image(url.toExternalForm(), BOSS_SIZE * 2, BOSS_SIZE * 2, true, true);
		});
	}
	
	private double sizeOf(int v) {
		return gen.getType(v) == EventType.BOSS ? BOSS_SIZE : NODE_SIZE;
	}
	
	private void addLine(Pane map, int from, int to, Set<Integer> visited) {
		double x1 = x(from), y1 = y(from);
		double x2 = x(to), y2 = y(to);
		
		double dx = x2 - x1, dy = y2 - y1;
		double len = Math.hypot(dx, dy);
		double ux = dx / len, uy = dy / len;
		
		double startOffset = sizeOf(from) / 2 + LINE_GAP;
		double endOffset = sizeOf(to) / 2 + LINE_GAP;
		
		Line line = new Line(x1 + ux * startOffset, y1 + uy * startOffset, x2 - ux * endOffset, y2 - uy * endOffset);
		
		boolean onPath = visited.contains(from) && visited.contains(to);
		line.setStroke(onPath ? Color.web("#1a1a2e") : Color.web("#5a5a5a"));
		line.setStrokeWidth(4);
		if (!onPath) 
			line.getStrokeDashArray().addAll(16.0, 12.0);
		line.setMouseTransparent(true);
		map.getChildren().add(line);
	}

	private double x(int v) {
		int count = gen.levelSize(gen.levelOf(v));
		double slot = (W - 2 * MARGIN_X) / count;
		return MARGIN_X + (gen.indexOf(v) + 0.5) * slot;
	}
	
	private double y(int v) {
		return contentHeight - MARGIN_Y - gen.levelOf(v) * rowGap;
	}
}
