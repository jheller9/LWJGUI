package lwjgui.scene.layout;

import org.joml.Vector2f;

import lwjgui.collections.ObservableList;
import lwjgui.event.ElementCallback;
import lwjgui.geometry.Orientation;
import lwjgui.scene.FillableRegion;
import lwjgui.scene.Node;

public class FlowPane extends FillableRegion {
	private Orientation orientation = Orientation.HORIZONTAL;
	private ObservableList<Node> items = new ObservableList<Node>();
	
	private Vector2f lastSize = new Vector2f();

	private DirectionalBox internalBox;
	private float hgap = 4;
	private float vgap = 4;
	
	public FlowPane() {
		this(Orientation.HORIZONTAL);
	}
	
	public FlowPane(Orientation orientation) {
		this.orientation = orientation;
		
		this.items.setAddCallback(new ElementCallback<Node>() {
			@Override
			public void onEvent(Node object) {
				rebuild();
			}
		});
		this.items.setRemoveCallback(new ElementCallback<Node>() {
			@Override
			public void onEvent(Node object) {
				rebuild();
			}
		});
	}
	
	public float getVgap() {
		return this.vgap;
	}
	
	public float getHgap() {
		return this.hgap;
	}
	
	public void setVgap(float spacing) {
		this.vgap = spacing;
		rebuild();
	}
	
	public void setHgap(float spacing) {
		this.hgap = spacing;
		rebuild();
	}
	
	@Override
	protected void position(Node parent) {
		float wid = (float) this.getWidth();
		float hei = (float) this.getHeight();
		
		if ( wid != lastSize.x || hei != lastSize.y ) {
			lastSize.set(wid, hei);
			rebuild();
			position(parent);
		}
		
		super.position(parent);
	}
	
	protected void rebuild() {
		this.getChildren().clear();
		
		this.internalBox = new VBox();
		if ( this.orientation.equals(Orientation.VERTICAL ) )
			this.internalBox = new HBox();
		this.internalBox.setSpacing((this.internalBox instanceof HBox)?hgap:vgap);
		this.internalBox.setFillToParentWidth(true);
		this.internalBox.setFillToParentHeight(true);
		this.getChildren().add(this.internalBox);
		
		DirectionalBox current = new HBox();
		if ( this.orientation.equals(Orientation.VERTICAL ) )
			current = new VBox();
		current.setSpacing((current instanceof HBox)?hgap:vgap);
		this.internalBox.getChildren().add(current);
		
		float currentLen = 0;
		for (int i = 0; i < items.size(); i++) {
			Node item = items.get(i);
			
			float cLen = (float) item.getWidth();
			float maxLen = (float) this.getWidth();
			if ( this.orientation.equals(Orientation.VERTICAL ) ) {
				cLen = (float) item.getHeight();
				maxLen = (float) this.getHeight();
			}
			currentLen += cLen;
			
			if ( currentLen <= maxLen || current.getChildren().size() == 0) {
				current.getChildren().add(item);
				currentLen += current.getSpacing();
			} else {
				current = new HBox();
				if ( this.orientation.equals(Orientation.VERTICAL ) )
					current = new VBox();
				current.setSpacing((current instanceof HBox)?hgap:vgap);
				this.internalBox.getChildren().add(current);
				currentLen = 0;
				
				// Rerun for this item
				i--;
			}
		}
	}

	public Orientation getOrientation() {
		return this.orientation;
	}
	
	public void setOrientation( Orientation orientation ) {
		this.orientation = orientation;
	}
	
	public ObservableList<Node> getItems() {
		return this.items;
	}

	@Override
	public boolean isResizeable() {
		return false;
	}
}
