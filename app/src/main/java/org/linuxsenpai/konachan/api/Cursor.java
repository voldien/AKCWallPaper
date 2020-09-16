package org.linuxsenpai.konachan.api;

import androidx.annotation.NonNull;

import org.json.JSONException;

public class Cursor<T> {
	public String name;
	public int current;
	private MetaController controller;
	private APICommand type;

	public Cursor(MetaController controller, @NonNull String tag, APICommand type) {
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
		return Math.max(current, 0) + 100;//controller.getCount();
	}

	public boolean hasNext() {
		return true;
	}

	public boolean hasPrevious() {
		return true;
	}

	public T current() {
		return null;//getIndex(current);
	}

	public T getIndex(int index) throws JSONException {
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
