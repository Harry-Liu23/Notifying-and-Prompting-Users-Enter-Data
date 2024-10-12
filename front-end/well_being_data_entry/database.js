import * as SQLite from 'expo-sqlite/legacy';

// Open the SQLite database (or create it if it doesn't exist)
const db = SQLite.openDatabase('records.db');

// Function to create the "records" table if it doesn't exist
export const createTable = () => {
  db.transaction(tx => {
    tx.executeSql(
      `CREATE TABLE IF NOT EXISTS records (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        mood TEXT,
        waterIntake TEXT,
        other TEXT
      );`
    );
  });
};

// Function to insert data into the "records" table
export const saveDataToDB = (mood, waterIntake, other, successCallback, errorCallback) => {
  db.transaction(tx => {
    tx.executeSql(
      `INSERT INTO records (mood, waterIntake, other) values (?, ?, ?);`,
      [mood, waterIntake, other],
      (txObj, resultSet) => successCallback && successCallback(resultSet),
      (txObj, error) => errorCallback && errorCallback(error)
    );
  });
};

// Function to retrieve all records from the database
export const getAllRecords = (callback) => {
  db.transaction(tx => {
    tx.executeSql(
      'SELECT * FROM records;',
      [],
      (txObj, { rows: { _array } }) => callback(_array),  // Return the results to the caller
      (txObj, error) => {
        console.log("Error retrieving data: ", error);
      }
    );
  });
};
