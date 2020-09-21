package org.linuxsenpai.konachan.api;

import androidx.annotation.NonNull;

import org.json.JSONException;

public class Cursor<T> {
	public String name;
	public int current;
	private MetaController controller;
	private final API type;

	public Cursor(MetaController controller, @NonNull String tag, API type) {
		this.controller = controller;
		this.type = type;
		this.current = 0;
		this.name = tag;
	}

	public T previous() throws JSONException {
		current--;
		return getIndex(current);
	}

	public T next() throws JSONException {
		current++;
		return getIndex(current);
	}

	public int size() {
		return Math.max(current, 0) + 25;//controller.getCount();
	}

	public boolean hasNext() {
		return true;
	}

	public boolean hasPrevious() {
		return true;
	}

	public T current() throws JSONException {
		return getIndex(current);
	}

	public T getIndex(int index) {
		assert name != null;

		switch (type) {
			case POST:
				return (T) controller.getPost(name, index);
			case NOTE:
				return (T) controller.getPost(name, index);
			default:
				throw new RuntimeException("");
		}
	}
}
