package com.example.gifs_watcher.database.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.gifs_watcher.models.User;
import java.lang.Boolean;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@SuppressWarnings({"unchecked", "deprecation"})
public final class UserDao_Impl implements UserDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<User> __insertionAdapterOfUser;

  private final EntityDeletionOrUpdateAdapter<User> __deletionAdapterOfUser;

  public UserDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUser = new EntityInsertionAdapter<User>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `users` (`username`,`password`,`name`,`lastname`,`mail`,`bio`,`birthdate`,`picture`,`created`,`id`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @Nullable final User entity) {
        if (entity.getUsername() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getUsername());
        }
        if (entity.getPassword() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getPassword());
        }
        if (entity.getName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getName());
        }
        if (entity.getLastName() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getLastName());
        }
        if (entity.getMail() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getMail());
        }
        if (entity.getBio() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getBio());
        }
        if (entity.getBirthdate() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getBirthdate());
        }
        if (entity.getProfilPicture() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getProfilPicture());
        }
        if (entity.getCreated() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getCreated());
        }
        if (entity.getIdUsers() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getIdUsers());
        }
      }
    };
    this.__deletionAdapterOfUser = new EntityDeletionOrUpdateAdapter<User>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `users` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @Nullable final User entity) {
        if (entity.getIdUsers() == null) {
          statement.bindNull(1);
        } else {
          statement.bindLong(1, entity.getIdUsers());
        }
      }
    };
  }

  @Override
  public Object insertUser(final User user, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUser.insert(user);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteUser(final User user, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfUser.handle(user);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllUsers(final Continuation<? super List<User>> $completion) {
    final String _sql = "SELECT * FROM users";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<User>>() {
      @Override
      @NonNull
      public List<User> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
          final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfLastName = CursorUtil.getColumnIndexOrThrow(_cursor, "lastname");
          final int _cursorIndexOfMail = CursorUtil.getColumnIndexOrThrow(_cursor, "mail");
          final int _cursorIndexOfBio = CursorUtil.getColumnIndexOrThrow(_cursor, "bio");
          final int _cursorIndexOfBirthdate = CursorUtil.getColumnIndexOrThrow(_cursor, "birthdate");
          final int _cursorIndexOfProfilPicture = CursorUtil.getColumnIndexOrThrow(_cursor, "picture");
          final int _cursorIndexOfCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "created");
          final int _cursorIndexOfIdUsers = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final List<User> _result = new ArrayList<User>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final User _item;
            final String _tmpUsername;
            if (_cursor.isNull(_cursorIndexOfUsername)) {
              _tmpUsername = null;
            } else {
              _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
            }
            final String _tmpPassword;
            if (_cursor.isNull(_cursorIndexOfPassword)) {
              _tmpPassword = null;
            } else {
              _tmpPassword = _cursor.getString(_cursorIndexOfPassword);
            }
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpLastName;
            if (_cursor.isNull(_cursorIndexOfLastName)) {
              _tmpLastName = null;
            } else {
              _tmpLastName = _cursor.getString(_cursorIndexOfLastName);
            }
            final String _tmpMail;
            if (_cursor.isNull(_cursorIndexOfMail)) {
              _tmpMail = null;
            } else {
              _tmpMail = _cursor.getString(_cursorIndexOfMail);
            }
            final String _tmpBio;
            if (_cursor.isNull(_cursorIndexOfBio)) {
              _tmpBio = null;
            } else {
              _tmpBio = _cursor.getString(_cursorIndexOfBio);
            }
            final String _tmpBirthdate;
            if (_cursor.isNull(_cursorIndexOfBirthdate)) {
              _tmpBirthdate = null;
            } else {
              _tmpBirthdate = _cursor.getString(_cursorIndexOfBirthdate);
            }
            final String _tmpProfilPicture;
            if (_cursor.isNull(_cursorIndexOfProfilPicture)) {
              _tmpProfilPicture = null;
            } else {
              _tmpProfilPicture = _cursor.getString(_cursorIndexOfProfilPicture);
            }
            final String _tmpCreated;
            if (_cursor.isNull(_cursorIndexOfCreated)) {
              _tmpCreated = null;
            } else {
              _tmpCreated = _cursor.getString(_cursorIndexOfCreated);
            }
            _item = new User(_tmpUsername,_tmpPassword,_tmpName,_tmpLastName,_tmpMail,_tmpBio,_tmpBirthdate,_tmpProfilPicture,_tmpCreated);
            final Integer _tmpIdUsers;
            if (_cursor.isNull(_cursorIndexOfIdUsers)) {
              _tmpIdUsers = null;
            } else {
              _tmpIdUsers = _cursor.getInt(_cursorIndexOfIdUsers);
            }
            _item.setIdUsers(_tmpIdUsers);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getUserByMail(final String mail, final Continuation<? super User> $completion) {
    final String _sql = "SELECT * FROM users WHERE LOWER(mail) = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (mail == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, mail);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<User>() {
      @Override
      @NonNull
      public User call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
          final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfLastName = CursorUtil.getColumnIndexOrThrow(_cursor, "lastname");
          final int _cursorIndexOfMail = CursorUtil.getColumnIndexOrThrow(_cursor, "mail");
          final int _cursorIndexOfBio = CursorUtil.getColumnIndexOrThrow(_cursor, "bio");
          final int _cursorIndexOfBirthdate = CursorUtil.getColumnIndexOrThrow(_cursor, "birthdate");
          final int _cursorIndexOfProfilPicture = CursorUtil.getColumnIndexOrThrow(_cursor, "picture");
          final int _cursorIndexOfCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "created");
          final int _cursorIndexOfIdUsers = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final User _result;
          if (_cursor.moveToFirst()) {
            final String _tmpUsername;
            if (_cursor.isNull(_cursorIndexOfUsername)) {
              _tmpUsername = null;
            } else {
              _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
            }
            final String _tmpPassword;
            if (_cursor.isNull(_cursorIndexOfPassword)) {
              _tmpPassword = null;
            } else {
              _tmpPassword = _cursor.getString(_cursorIndexOfPassword);
            }
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpLastName;
            if (_cursor.isNull(_cursorIndexOfLastName)) {
              _tmpLastName = null;
            } else {
              _tmpLastName = _cursor.getString(_cursorIndexOfLastName);
            }
            final String _tmpMail;
            if (_cursor.isNull(_cursorIndexOfMail)) {
              _tmpMail = null;
            } else {
              _tmpMail = _cursor.getString(_cursorIndexOfMail);
            }
            final String _tmpBio;
            if (_cursor.isNull(_cursorIndexOfBio)) {
              _tmpBio = null;
            } else {
              _tmpBio = _cursor.getString(_cursorIndexOfBio);
            }
            final String _tmpBirthdate;
            if (_cursor.isNull(_cursorIndexOfBirthdate)) {
              _tmpBirthdate = null;
            } else {
              _tmpBirthdate = _cursor.getString(_cursorIndexOfBirthdate);
            }
            final String _tmpProfilPicture;
            if (_cursor.isNull(_cursorIndexOfProfilPicture)) {
              _tmpProfilPicture = null;
            } else {
              _tmpProfilPicture = _cursor.getString(_cursorIndexOfProfilPicture);
            }
            final String _tmpCreated;
            if (_cursor.isNull(_cursorIndexOfCreated)) {
              _tmpCreated = null;
            } else {
              _tmpCreated = _cursor.getString(_cursorIndexOfCreated);
            }
            _result = new User(_tmpUsername,_tmpPassword,_tmpName,_tmpLastName,_tmpMail,_tmpBio,_tmpBirthdate,_tmpProfilPicture,_tmpCreated);
            final Integer _tmpIdUsers;
            if (_cursor.isNull(_cursorIndexOfIdUsers)) {
              _tmpIdUsers = null;
            } else {
              _tmpIdUsers = _cursor.getInt(_cursorIndexOfIdUsers);
            }
            _result.setIdUsers(_tmpIdUsers);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getUserByUsername(final String username,
      final Continuation<? super User> $completion) {
    final String _sql = "SELECT * FROM users WHERE LOWER(username) = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (username == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, username);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<User>() {
      @Override
      @NonNull
      public User call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
          final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfLastName = CursorUtil.getColumnIndexOrThrow(_cursor, "lastname");
          final int _cursorIndexOfMail = CursorUtil.getColumnIndexOrThrow(_cursor, "mail");
          final int _cursorIndexOfBio = CursorUtil.getColumnIndexOrThrow(_cursor, "bio");
          final int _cursorIndexOfBirthdate = CursorUtil.getColumnIndexOrThrow(_cursor, "birthdate");
          final int _cursorIndexOfProfilPicture = CursorUtil.getColumnIndexOrThrow(_cursor, "picture");
          final int _cursorIndexOfCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "created");
          final int _cursorIndexOfIdUsers = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final User _result;
          if (_cursor.moveToFirst()) {
            final String _tmpUsername;
            if (_cursor.isNull(_cursorIndexOfUsername)) {
              _tmpUsername = null;
            } else {
              _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
            }
            final String _tmpPassword;
            if (_cursor.isNull(_cursorIndexOfPassword)) {
              _tmpPassword = null;
            } else {
              _tmpPassword = _cursor.getString(_cursorIndexOfPassword);
            }
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpLastName;
            if (_cursor.isNull(_cursorIndexOfLastName)) {
              _tmpLastName = null;
            } else {
              _tmpLastName = _cursor.getString(_cursorIndexOfLastName);
            }
            final String _tmpMail;
            if (_cursor.isNull(_cursorIndexOfMail)) {
              _tmpMail = null;
            } else {
              _tmpMail = _cursor.getString(_cursorIndexOfMail);
            }
            final String _tmpBio;
            if (_cursor.isNull(_cursorIndexOfBio)) {
              _tmpBio = null;
            } else {
              _tmpBio = _cursor.getString(_cursorIndexOfBio);
            }
            final String _tmpBirthdate;
            if (_cursor.isNull(_cursorIndexOfBirthdate)) {
              _tmpBirthdate = null;
            } else {
              _tmpBirthdate = _cursor.getString(_cursorIndexOfBirthdate);
            }
            final String _tmpProfilPicture;
            if (_cursor.isNull(_cursorIndexOfProfilPicture)) {
              _tmpProfilPicture = null;
            } else {
              _tmpProfilPicture = _cursor.getString(_cursorIndexOfProfilPicture);
            }
            final String _tmpCreated;
            if (_cursor.isNull(_cursorIndexOfCreated)) {
              _tmpCreated = null;
            } else {
              _tmpCreated = _cursor.getString(_cursorIndexOfCreated);
            }
            _result = new User(_tmpUsername,_tmpPassword,_tmpName,_tmpLastName,_tmpMail,_tmpBio,_tmpBirthdate,_tmpProfilPicture,_tmpCreated);
            final Integer _tmpIdUsers;
            if (_cursor.isNull(_cursorIndexOfIdUsers)) {
              _tmpIdUsers = null;
            } else {
              _tmpIdUsers = _cursor.getInt(_cursorIndexOfIdUsers);
            }
            _result.setIdUsers(_tmpIdUsers);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object isEmailUsed(final String mail, final Continuation<? super Boolean> $completion) {
    return UserDao.DefaultImpls.isEmailUsed(UserDao_Impl.this, mail, $completion);
  }

  @Override
  public Object isUsernameUsed(final String username,
      final Continuation<? super Boolean> $completion) {
    return UserDao.DefaultImpls.isUsernameUsed(UserDao_Impl.this, username, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
