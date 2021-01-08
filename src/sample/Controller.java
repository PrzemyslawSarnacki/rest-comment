package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


import java.util.List;


public class Controller {
    @FXML
    private ComboBox<Integer> comboBox;

    @FXML
    private TableView tableView;

    @FXML
    private TextArea commentText;

    @FXML
    private TextField authorText;

    @FXML
    private TableColumn<Comment, Long> idCol;

    @FXML
    private TableColumn<Comment, String> authorCol;

    @FXML
    private TableColumn<Comment, String> commentCol;

    private String indicator;

    public ObservableList<Comment> data;


    @FXML
    public void initialize() {
        initComboBox();
        getData(40);
//        refreshTable();

    }

    public void initComboBox() {
        ObservableList<Integer> numberList = FXCollections.observableArrayList();
        numberList.add(5);
        numberList.add(20);
        numberList.add(40);
        comboBox.setItems(numberList);
    }

    private void refreshTable() {
        if (idCol != null) {

            idCol.setCellValueFactory(new PropertyValueFactory<Comment, Long>("id"));
            authorCol.setCellValueFactory(new PropertyValueFactory<Comment, String>("author"));
            commentCol.setCellValueFactory(new PropertyValueFactory<Comment, String>("comment"));

            tableView.setItems(data);
        }

    }

    public void onDownloadClick() {
        getData(40);
    }

    public void onSizeChange() {
        getData(comboBox.getValue());
    }

    public void onAddComment() {
        String author = authorText.getText();
        String commentTxt = commentText.getText();

        Comment comment = new Comment(author, commentTxt);

        postData(comment);
        onDownloadClick();


    }


    private void postData(Comment comment) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);  // <-- this is the important line!


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://51.83.134.180:8090/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<Comment> call = apiClient.addPost(comment);


        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Code: " + response.code());

                    return;

                }
                Comment comments = response.body();
                data = FXCollections.observableArrayList();
                authorText.clear();
                commentText.clear();

            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                System.out.println(t.getMessage());
                return;
            }


        });

    }
        private void getData(int dataSize) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

            logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(logging);  // <-- this is the important line!

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://51.83.134.180:8090/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
            ApiClient apiClient = retrofit.create(ApiClient.class);
            Call<List<Comment>> call = apiClient.getPosts(dataSize);


            call.enqueue(new Callback<List<Comment>>() {
                @Override
                public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                    if (!response.isSuccessful()) {
                        System.out.println("Code: " + response.code());
                        return;
                    }
                    List<Comment> comments = response.body();
                    data = FXCollections.observableArrayList();
                    for (Comment comment : comments) {
                        data.add(comment);
                    }
                    refreshTable();
                }

                @Override
                public void onFailure(Call<List<Comment>> call, Throwable t) {
                    System.out.println(t.getMessage());
                }


            });
        }
    }
