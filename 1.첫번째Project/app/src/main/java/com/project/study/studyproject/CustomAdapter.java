package com.project.study.studyproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>  {

        public ArrayList<Dictionary> arrayList;
        public Context mContext;
        public int position;
        public Dictionary dictionary;

        public CustomAdapter(Context mContext, ArrayList<Dictionary> arrayList) {
            this.arrayList = arrayList;
            this.mContext = mContext;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener { // 1. 리스너 추가
            Button b1, b2, b3;

            public TextView title, contents, date;

            public MyViewHolder(View view) {
                super(view);
                //initialize textViews and buttons
                contents = (TextView) view.findViewById(R.id.contents);
                date = (TextView) view.findViewById(R.id.date);

                b1 = (Button) view.findViewById(R.id.edit);
                b2 = (Button) view.findViewById(R.id.del);
                b3 = (Button) view.findViewById(R.id.gong);

                view.setOnCreateContextMenuListener(this); //2. 리스너 등록

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, MemoAdd.class);

                        int position = getAdapterPosition();
                        dictionary = arrayList.get(position);
                        intent.putExtra("contents", dictionary.getContents());
                        intent.putExtra("key", dictionary.getKey());
                        intent.putExtra("check", "edit");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                });
            }

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) { // 3. 메뉴 추가
                MenuItem Edit = menu.add(Menu.NONE, 1001, 1, "수정");
                android.view.MenuItem Delete = menu.add(Menu.NONE, 1002, 2, "삭제");
                Edit.setOnMenuItemClickListener(onEditMenu);
                Delete.setOnMenuItemClickListener(onEditMenu);
            }


            // 4. 캔텍스트 메뉴 클릭시 동작을 설정
            private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    switch (item.getItemId()) {
                        case 1001:

                            dictionary = arrayList.get(getAdapterPosition());
                            contents.setText(dictionary.getContents());

                            Intent intent = new Intent(mContext, MemoAdd.class);

                            //pass details to to be edited to next class using putExtra
                            intent.putExtra("contents", dictionary.getContents());
                            intent.putExtra("key", dictionary.getKey());
                            intent.putExtra("check","edit");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intent);

                            break;

                        case 1002:

                            final Dictionary dictionary = arrayList.get(getAdapterPosition());
                            DbHandler db = new DbHandler(mContext);
                            db.deleteRow(dictionary.getKey());
                            db.close();

                            arrayList.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            notifyItemRangeChanged(getAdapterPosition(), arrayList.size());
                            Toast.makeText(mContext, "메모가 삭제됨", Toast.LENGTH_SHORT).show();

                            break;
                    }
                    return true;
                }
            };

        }

    //ViewGroup인 parent 클래스에서 context 를 받고, inflate 하여 새로운 View 객체를 생성하고, 뷰홀더를 생성한다
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
            return new MyViewHolder(itemView);
        }

        //text 변수에 List 에 담긴 뷰에 설정할 데이터를 가져온다.  holder 의 View 안의 데이터를 변경하는 작업을 한다
        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {

            final Dictionary dictionary = arrayList.get(position);
            holder.contents.setText(dictionary.getContents());
            holder.date.setText(dictionary.getDate());

            //Onclick EDIT Button
            holder.b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MemoAdd.class);

                    //pass details to to be edited to next class using putExtra
                    intent.putExtra("contents", dictionary.getContents());
                    intent.putExtra("key", dictionary.getKey());
                    intent.putExtra("check","edit");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });

            //onClick DELETE Button
            holder.b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DbHandler db = new DbHandler(mContext);
                    db.deleteRow(dictionary.getKey());
                    db.close();

                    arrayList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, arrayList.size());
                    Toast.makeText(mContext, "메모가 삭제됨", Toast.LENGTH_SHORT).show();

                    //notifyItemRemoved : 특정 Position 에 데이터를 하나 제거할 경우
                    //notifyDataSetChanged : 데이터가 전체 바뀌었을 때 호출. 즉, 처음 부터 끝까지 전부 바뀌었을 경우
                    //notifyItemChanged : 특정 Position의 위치만 바뀌었을 경우. position 4 번 위치만 데이터가 바뀌었을 경우 사용 하면 된다
                    //notifyItemRangeChanged : 특정 영역을 데이터가 바뀌었을 경우. position 3~10번까지의 데이터만 바뀌었을 경우 사용 하면 된다
                    //notifyItemInserted : 특정 Position에 데이터 하나를 추가 하였을 경우. position 3번과 4번 사이에 넣고자 할경우 4를 넣으면 되겠죠
                    //notifyItemRangeInserted : 특정 영역에 데이터를 추가할 경우. position 3~10번 자리에 7개의 새로운 데이터를 넣을 경우
                    //notifyItemRangeRemoved : 특정 영역의 데이터를 제거할 경우
                    //notifyItemMoved : 특정 위치를 교환할 경우 (Drag and drop에 쓰이겠네요^^
                }
            });

            //Onclick 공유 Button
            holder.b3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.putExtra(Intent.EXTRA_SUBJECT, "다이어리");
                    intent.putExtra(Intent.EXTRA_TEXT, dictionary.getContents());
                    intent.putExtra(Intent.EXTRA_TITLE, "안녕하세요");
                    intent.setType("text/plain");
                    mContext.startActivity(Intent.createChooser(intent, "공유"));
                }
            });
        }

        //데이터의 총 개수를 반환한다
        @Override
        public int getItemCount() {
            return arrayList.size();
        }

    }


