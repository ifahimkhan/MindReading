/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ml.fahimkhan.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class GreenAdapter extends RecyclerView.Adapter<GreenAdapter.NumberViewHolder> {

    private static final String TAG = GreenAdapter.class.getSimpleName();
    private int mNumberItems, random;
    ArrayList<Integer> imagearry = new ArrayList<Integer>();
    Context mycontext, context;

    public ArrayList<Integer> pastindex=new ArrayList<>();
    ArrayList<Boolean> booleanitemClicked = new ArrayList<>();


    public GreenAdapter(Context context, int numberOfItems, int random, ArrayList<Integer> imagearry, ArrayList<Boolean> booleanitemClicked) {
        mNumberItems = numberOfItems;
        this.random = random;
        this.mycontext = context;
        this.context = context;
        this.imagearry = imagearry;
        this.booleanitemClicked = booleanitemClicked;
    }


    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.grid_number_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        NumberViewHolder viewHolder = new NumberViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final NumberViewHolder holder, final int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Will display the position in the list, ie 0 through getItemCount() - 1
        TextView listItemNumberView;
        ImageView imageView, imgvisible;
        View view;

        Dialog dialog;
        TextView showBtn, cancelBtn;
        ImageView imageViewmine;

        public NumberViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.image);
            imgvisible = (ImageView) itemView.findViewById(R.id.imgvisible);
            listItemNumberView = (TextView) itemView.findViewById(R.id.tv_item_number);
            imgvisible.setClickable(true);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
            imgvisible.setOnClickListener(this);

        }

        /*
                private void createDialog(int position) {
                    dialog = new Dialog(mycontext);
                    //SET TITLE
                    dialog.setTitle("Icon");
                    //set content
                    dialog.setContentView(R.layout.custom_dialog);
                    imageViewmine = (ImageView) dialog.findViewById(R.id.imageView);
                    imageViewmine.setImageResource(imagearry.get(position));

                    showBtn = (TextView) dialog.findViewById(R.id.showTxt);
                    cancelBtn = (TextView) dialog.findViewById(R.id.cancelTxt);
                }
        */
        void bind(int listIndex) {
            listItemNumberView.setText(String.valueOf(listIndex));
            if (booleanitemClicked.get(listIndex)) {
                imgvisible.setImageResource(imagearry.get(listIndex));
            }else{
                imgvisible.setImageResource(R.drawable.ic_action_name);
            }

        }

        @Override
        public void onClick(View view) {
            if(!pastindex.isEmpty()){
                booleanitemClicked.set(pastindex.get(0),false);
                notifyItemChanged(pastindex.get(0));
                pastindex.clear();
            }

            booleanitemClicked.set(getAdapterPosition(), true);
            notifyItemChanged(getAdapterPosition());
            pastindex.add(getAdapterPosition());


        }
    }
}
