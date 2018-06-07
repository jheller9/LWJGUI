package lwjgui.scene.control;

import lwjgui.Context;
import lwjgui.collections.ObservableList;
import lwjgui.event.ChangeEvent;
import lwjgui.event.MouseEvent;

public class Menu extends MenuItem {
	
	private ObservableList<MenuItem> items = new ObservableList<MenuItem>();
	private ContextMenu context;
	
	public Menu(String string) {
		super(string, null);
		
		context = new ContextMenu();
		context.setAutoHide(true);
		
		this.items.setAddCallback(new ChangeEvent<MenuItem>() {
			@Override
			public void onEvent(MenuItem changed) {
				context.getItems().add(changed);
			}
		});
		
		this.items.setRemoveCallback(new ChangeEvent<MenuItem>() {
			@Override
			public void onEvent(MenuItem changed) {
				context.getItems().remove(changed);
			}
		});
		
		this.mousePressedEvent = new MouseEvent() {
			@Override
			public void onEvent(int button) {
				open();
			}
		};
		
		this.mouseReleasedEvent = null;
	}
	
	public boolean isOpen() {
		return this.context.isOpen();
	}

	public ObservableList<MenuItem> getItems() {
		return this.items;
	}
	
	@Override
	public void render(Context context) {
		super.render(context);
		
		//System.out.println(context.getHovered());
	}

	public void open() {
		((MenuBar)(getParent().getParent())).isOpen = true;
		((MenuBar)(getParent().getParent())).currentMenu = Menu.this;
		context.show(Menu.this.getScene(), getAbsoluteX(), getAbsoluteY()+getHeight());
	}

	public void close() {
		((MenuBar)(getParent().getParent())).isOpen = false;
		context.close();
	}
}
