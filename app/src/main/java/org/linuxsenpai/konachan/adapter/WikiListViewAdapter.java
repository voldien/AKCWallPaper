package org.linuxsenpai.konachan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import org.linuxsenpai.konachan.R;
import org.linuxsenpai.konachan.db.Wiki;
import org.linuxsenpai.konachan.fragment.WikiPageFragment;

import java.util.ArrayList;
import java.util.List;

public class WikiListViewAdapter extends RecyclerView.Adapter<WikiListViewAdapter.ViewHolder> {

	private final List<Wiki> data;

	public WikiListViewAdapter(Context context) {
		data = new ArrayList<>();
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(viewType, parent, false);
		return new WikiListViewAdapter.ViewHolder(view);
	}

	@Override
	public int getItemViewType(final int position) {
		return R.layout.item_wiki_view;
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		holder.bind(data.get(position));
	}

	@Override
	public int getItemCount() {
		return data.size();
	}

	public void addItemList(List<Wiki> list) {
		this.data.addAll(list);
		notifyDataSetChanged();
	}

	public void clear() {
		this.data.clear();
		notifyDataSetChanged();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		public final View mParentView;
		public final TextView mTextTitleView;
		public Wiki wiki;

		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			this.mParentView = itemView;
			this.mTextTitleView = itemView.findViewById(R.id.wiki_text_title);
		}

		public void bind(Wiki wiki) {
			if (wiki == null) {

			} else {
				this.wiki = wiki;
				this.mTextTitleView.setText(wiki.title);
				this.mTextTitleView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {

						AppCompatActivity activity = (AppCompatActivity) v.getContext();
						if (activity != null) {
							FragmentManager fragmentManager = activity.getSupportFragmentManager();
							FragmentTransaction transaction = fragmentManager.beginTransaction();
							transaction.setReorderingAllowed(true); //TODO setAllowOptimization before 26.1.0

							transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
							transaction.setCustomAnimations(R.anim.nav_default_enter_anim, R.anim.nav_default_exit_anim, R.anim.nav_default_pop_enter_anim, R.anim.nav_default_pop_exit_anim);
							transaction.add(WikiPageFragment.newInstance(wiki), "");
							transaction.addToBackStack(null);
							transaction.commit();
						}
					}
				});
			}
		}
	}
}
