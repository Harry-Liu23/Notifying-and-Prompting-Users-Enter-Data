import { Share, StyleSheet, Text, View, TextInput, Button, Alert } from 'react-native';
import React, { useState } from 'react';
import * as FileSystem from 'expo-file-system';


const Record = () => {
  const [input1, setInput1] = useState('');
  const [input2, setInput2] = useState('');
  const [input3, setInput3] = useState('');

  const saveDataToFile = async () => {
    try {
      const data = `Mood: ${input1}\nWater Intake: ${input2}\nOther: ${input3}`;
      const path = `${FileSystem.documentDirectory}recordData.txt`;
  
      await FileSystem.writeAsStringAsync(path, data);
      Alert.alert('Success', 'Data saved to file successfully!');
      return path;
    } catch (err) {
      console.log(err);
      Alert.alert('Error', 'Failed to save data to file.');
    }
  };

  const exportFile = async () => {
    try {
      const filePath = await saveDataToFile();
      if (filePath) {
        // Use Share API to share the file
        await Share.share({
          message: `Sharing data file located at: ${filePath}`, // Message to share
          url: filePath, // File path to share
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
        placeholder="Enter first input"
        value={input1}
        onChangeText={setInput1}
      />

      <Text style={styles.label}>Enter your water intake:</Text>
      <TextInput
        style={styles.input}
        placeholder="Enter second input"
        value={input2}
        onChangeText={setInput2}
      />

      <Text style={styles.label}>I'm running out of things to talk about:</Text>
      <TextInput
        style={styles.input}
        placeholder="Enter third input"
        value={input3}
        onChangeText={setInput3}
      />
      
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
