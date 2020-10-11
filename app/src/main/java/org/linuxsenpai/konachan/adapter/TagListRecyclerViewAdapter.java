package org.linuxsenpai.konachan.adapter;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.linuxsenpai.konachan.R;
import org.linuxsenpai.konachan.db.Tag;

import java.util.List;

//TODO rename
public class TagListRecyclerViewAdapter extends RecyclerView.Adapter<TagListRecyclerViewAdapter.ViewHolder> {

	private final List<Tag> tagList;
	private Context context;

	public TagListRecyclerViewAdapter(List<Tag> items, Context context) {
		tagList = items;
		this.context = context;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.fragment_tag_list_item, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		//holder.mItem = mValues.get(position);
		holder.mIdView.setText(String.valueOf(tagList.get(position).count));
		holder.mContentView.setText(tagList.get(position).name);

		int[] color = context.getResources().getIntArray(R.array.tag_colors);
		holder.mContentView.setTextColor(color[tagList.get(position).type]);

		holder.mView.setAnimation(AnimationUtils.loadAnimation(holder.mView.getContext(), R.anim.anim_enter_slide_left));
//		holder.mView.setAnimation(AnimationUtils.loadAnimation(holder.mView.getContext(), R.anim.nav_default_pop_enter_anim));
	}

	@Override
	public int getItemCount() {
		return tagList.size();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
		public View mView;
		public TextView mIdView;
		public TextView mContentView;
		public Tag item;

		public ViewHolder(View view) {
			super(view);
			mView = view;
			mIdView = view.findViewById(R.id.textview_tag_number);
			mContentView = view.findViewById(R.id.textview_tag_name);

			mContentView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_SEARCH);
					intent.putExtra(SearchManager.QUERY, mContentView.getText());
					//TODO Resolve.
					//MainActivity mainActivity = ((Activity) (mIdView.getContext());
					//	mainActivity.startIntentSender();
					//((Activity) (mIdView.getContext())).setIntent(intent);
				}
			});
			//TODO add menu
			mView.setOnCreateContextMenuListener(this);
/*			mView.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					*//*  Make menu option display.   *//*
					v.showContextMenu();
					*//*  *//*
					return false;
				}
			});*/
		}

		@Override
		public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
			menu.setHeaderTitle("Select The Action");
			menu.add(R.string.search).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					return true;
				}
			});
			menu.add(0, v.getId(), 0, "B").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					return true;
				}
			});

		}

		@Override
		public String toString() {
			return super.toString() + " '" + mContentView.getText() + "'";
		}
	}
}
