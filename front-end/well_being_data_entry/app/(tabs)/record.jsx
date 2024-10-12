import React, { useState, useEffect } from 'react';
import { Share, StyleSheet, Text, View, TextInput, Button, Alert } from 'react-native';
import * as FileSystem from 'expo-file-system';
import { createTable, saveDataToDB, getAllRecords } from '../../database.js';

const Record = () => {
  const [input1, setInput1] = useState('');
  const [input2, setInput2] = useState('');
  const [input3, setInput3] = useState('');

  useEffect(() => {
    // Create the table when the app loads
    createTable();
  }, []);

  const saveData = () => {
    if (input1 || input2 || input3) {
      saveDataToDB(input1, input2, input3,
        () => {
          Alert.alert('Success', 'Data saved successfully!');
          setInput1('');
          setInput2('');
          setInput3('');
        },
        (error) => {
          console.log(error);
          Alert.alert('Error', 'Failed to save data.');
        }
      );
    } else {
      Alert.alert('Error', 'Please provide some data to save.');
    }
  };

  const generateFileFromDB = async () => {
    return new Promise((resolve, reject) => {
      getAllRecords(async (records) => {
        let data = 'Mood,Water Intake,Other\n';
        records.forEach(record => {
          data += `${record.mood},${record.waterIntake},${record.other}\n`;
        });

        try {
          const path = `${FileSystem.documentDirectory}recordData.txt`;
          await FileSystem.writeAsStringAsync(path, data);
          resolve(path);
        } catch (err) {
          reject(err);
        }
      });
    });
  };

  const exportFile = async () => {
    try {
      const filePath = await generateFileFromDB();
      if (filePath) {
        await Share.share({
          message: `Sharing data file located at: ${filePath}`,
          url: filePath,
        });
      }
    } catch (err) {
      console.log(err);
      Alert.alert('Error', 'Failed to share the file.');
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Record Data</Text>

      <Text style={styles.label}>Enter Your Mood here:</Text>
      <TextInput
        style={styles.input}
        placeholder="Enter mood"
        value={input1}
        onChangeText={setInput1}
      />

      <Text style={styles.label}>Enter your water intake:</Text>
      <TextInput
        style={styles.input}
        placeholder="Enter water intake"
        value={input2}
        onChangeText={setInput2}
      />

      <Text style={styles.label}>Other:</Text>
      <TextInput
        style={styles.input}
        placeholder="Enter other info"
        value={input3}
        onChangeText={setInput3}
      />

      <Button title="Save" onPress={saveData} />
      <Button title="Export" onPress={exportFile} />
    </View>
  );
};

export default Record;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 16,
    backgroundColor: '#fff',
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 16,
    textAlign: 'center',
  },
  label: {
    fontSize: 18,
    marginBottom: 8,
  },
  input: {
    height: 40,
    borderColor: 'gray',
    borderWidth: 1,
    paddingLeft: 8,
    marginBottom: 16,
    borderRadius: 4,
  },
});
