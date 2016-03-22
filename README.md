For android. 简化数据库操作

Add your content here.

Details

Step 1:创建实体对象 Java代码 @Table(name = "book") public class Book {

@Id private Long id;

@Column(name = "book_name") private String bookName;

public Book() {

}

public Long getId() {

return id;

}

public void setId(Long id) {

this.id = id;

}

public String getBookName() {

return bookName;

}

public void setBookName(String bookName) {

this.bookName = bookName;

}

}

Step 2:创建 DatabaseHelper Java代码 public class DatabaseHelper extends SQLiteOpenHelper {

public static final String BOOK_TABLE_NAME = "orm_book";

public static final String DATABASE_NAME = "OrmDemo.db";

public DatabaseHelper(Context context) {

super(context, DATABASE_NAME, null, 1);

}

@Override public void onCreate(SQLiteDatabase db) {

TableUtils.createTable(db, true, Book.class);

}

@Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

TableUtils.dropTable(db, Book.class); onCreate(db);

}

}

Step 3:创建 DatabaseService Java代码 public class DatabaseService {

private Context mContext;

private DatabaseHelper mDatabaseHelper;

private SQLiteDatabase mSQLiteDatabase;

private AhibernateDao

<Book>

mBookDao;

public DatabaseService(Context context) {

this.mContext = context; this.mDatabaseHelper = new DatabaseHelper(mContext); this.mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();

this.mBookDao = new AhibernateDao

<Book>

(this.mSQLiteDatabase);

}

// ===================book begin=========================== public List

<Book>

getBooksByWhere(Map where) {

List

<Book>

bookList = mBookDao.queryList(Book.class, where);

return bookList;

}

public List

<Book>

getBooks(Book book) {

List

<Book>

bookList = mBookDao.queryList(book);

return bookList;

}

public int addBook(Book book) {

return mBookDao.insert(book);

}

public void updateBook(Book book, Map where) {

mBookDao.update(book, where);

}

public void deleteBook(Map where) {

mBookDao.delete(Book.class, where);

} // ===================book end===============================

Step 4:创建Activity Java代码 public class OrmDemoActivity extends Activity {

/ Called when the activity is first created. / DatabaseService mDatabaseService;

Button mAddButton;

Button mQueryButton;

TextView mTextView;

@Override public void onCreate(Bundle savedInstanceState) {

super.onCreate(savedInstanceState); setContentView(R.layout.main); mAddButton = (Button) findViewById(R.id.add); mQueryButton = (Button) findViewById(R.id.query); mTextView = (TextView) findViewById(R.id.count); mDatabaseService = new DatabaseService(this); mAddButton.setOnClickListener(new OnClickListener() {

@Override public void onClick(View arg0) {

Book book = new Book(); Random ra = new Random(); book.setId(ra.nextLong()); book.setBookName("demo"); mDatabaseService.addBook(book); Toast.makeText(OrmDemoActivity.this, "添加成功", Toast.LENGTH_SHORT).show();

}

});

mQueryButton.setOnClickListener(new OnClickListener() {

@Override public void onClick(View v) {

mTextView.setText("总共有" + mDatabaseService.getBooksByWhere(null).size() + "本书");

}

}); }
