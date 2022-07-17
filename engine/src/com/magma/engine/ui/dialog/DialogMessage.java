package com.magma.engine.ui.dialog;

public class DialogMessage {
	
	public static final String PLACEHOLDER = "No dialog found! Whoops...";
	
	public final DialogStyle style;
	public final String message;
	
	public DialogMessage(int key, DialogStyle style) {
		this.style = style;
		this.message = PLACEHOLDER;
	}
	
	public DialogMessage(int key) {
		this.message = PLACEHOLDER;
		this.style = DialogStyle.Basic;
	}

	public DialogMessage(String message, DialogStyle style) {
		this.message = message;
		this.style = style;
	}
	
	public DialogMessage(String message) {
		this.message = message;
		this.style = DialogStyle.Basic;
	}
}
