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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.linuxsenpai.konachan.R;
import org.linuxsenpai.konachan.db.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagListViewAdapter extends RecyclerView.Adapter<TagListViewAdapter.ViewHolder> {

	private final List<Tag> tagList;
	private final Context context;

	public TagListViewAdapter(Context context) {
		tagList = new ArrayList<>();
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

		Tag tag = tagList.get(position);
		holder.item = tag;
		holder.mIdView.setText(String.valueOf(tag.count));
		holder.mContentView.setText(tag.name);

		holder.mContentView.setTextColor(tag.getTagColor(context));

		holder.mView.setAnimation(AnimationUtils.loadAnimation(holder.mView.getContext(), R.anim.anim_enter_slide_left));
//		holder.mView.setAnimation(AnimationUtils.loadAnimation(holder.mView.getContext(), R.anim.nav_default_pop_enter_anim));
	}

	@Override
	public int getItemCount() {
		return tagList.size();
	}

	public void clear() {
		this.tagList.clear();
		this.notifyDataSetChanged();
	}

	public void addItems(List<Tag> tags) {
		this.tagList.addAll(tags);
		this.notifyDataSetChanged();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
		public final View mView;
		public final TextView mIdView;
		public final TextView mContentView;
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
					AppCompatActivity appCompatActivity = (AppCompatActivity) mIdView.getContext();
					if (appCompatActivity != null) {
//						appCompatActivity.startActivity(intent);
						//appCompatActivity.startIntentSender();
					}
				}
			});
			mView.setOnCreateContextMenuListener(this);
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
			menu.add(R.string.menu_context_wiki_page).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
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
