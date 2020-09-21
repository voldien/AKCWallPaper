package org.linuxsenpai.konachan.adapter;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.linuxsenpai.konachan.R;
import org.linuxsenpai.konachan.db.History;

public class HistoryAdapter extends PagedListAdapter<History, HistoryAdapter.HistoryViewHolder> {

	public static final DiffUtil.ItemCallback<History> DIFF_CALLBACK =
			new DiffUtil.ItemCallback<History>() {
				@Override
				public boolean areItemsTheSame(
						@NonNull History oldUser, @NonNull History newUser) {
					// User properties may have changed if reloaded from the DB, but ID is fixed
					return oldUser.uid == newUser.uid;
				}

				@Override
				public boolean areContentsTheSame(
						@NonNull History oldUser, @NonNull History newUser) {
					// NOTE: if you use equals, your object must properly override Object#equals()
					// Incorrectly returning false here will result in too many animations.
					return false;//Objects.equals(oldUser, newUser);
				}
			};

	public HistoryAdapter() {
		super(DIFF_CALLBACK);
	}

	@NonNull
	@Override
	public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_information_view, parent, false);

		view.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}
		});
		HistoryViewHolder holder = new HistoryViewHolder(view);
		//TODO add click event.
		return holder;
	}

	@Override
	public void onBindViewHolder(HistoryViewHolder holder, int position) {
		History user = getItem(position);
		if (user != null) {
			holder.bindTo(user);
		} else {
			// Null defines a placeholder item - PagedListAdapter will automatically invalidate
			// this row when the actual object is loaded from the database
			holder.clear();
		}
	}

	public static class HistoryViewHolder extends RecyclerView.ViewHolder {
		private TextView textView;

		public HistoryViewHolder(@NonNull View itemView) {
			super(itemView);
			textView = (TextView) itemView.findViewById(R.id.textview);
		}

		public void clear() {
		}

		public void bindTo(History history) {
			textView.setText(history.name);
		}
	}
}
