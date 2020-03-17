package com.example.collegeconnect;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Upload> noteslist;
    private Downloader downloader;

    public NotesAdapter(Context context, ArrayList<Upload> noteslist) {
        this.context = context;
        this.noteslist = noteslist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_notes,parent,false);
        return new NotesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Toast.makeText(context, position, Toast.LENGTH_SHORT).show();
        final Upload notes = noteslist.get(position);
        holder.title.setText(notes.getName());
        holder.author.setText(notes.getAuthor());
        holder.noOfDown.setText("No. of Downloads = "+String.valueOf(notes.getDownload()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String extStorageDirectory = Environment.getExternalStorageDirectory()
                        .toString();
                File folder = new File(extStorageDirectory, "pdf");
                folder.mkdir();
                File file = new File(folder, ""+notes.getName());
                try {
                    file.createNewFile();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                downloader.DownloadFile(notes.getUrl(),file);
                showPdf();

//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(notes.getUrl()));
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
            }
        });
    }
    public void showPdf()
    {
        File file = new File(Environment.getExternalStorageDirectory()+"/pdf/Read.pdf");
        PackageManager packageManager = context.getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/pdf");
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return noteslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title,author,noOfDown;
        View view ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.authorname);
            noOfDown = itemView.findViewById(R.id.download);
            this.view=itemView;
        }
    }
}
