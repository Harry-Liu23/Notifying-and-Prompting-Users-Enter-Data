import { StyleSheet, Text, View, TextInput } from 'react-native';
import React, { useState } from 'react';

const Record = () => {
  const [input1, setInput1] = useState('');
  const [input2, setInput2] = useState('');
  const [input3, setInput3] = useState('');

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Record Data</Text>

      <Text style={styles.label}>Input 1:</Text>
      <TextInput
        style={styles.input}
        placeholder="Enter first input"
        value={input1}
        onChangeText={setInput1}
      />

      <Text style={styles.label}>Input 2:</Text>
      <TextInput
        style={styles.input}
        placeholder="Enter second input"
        value={input2}
        onChangeText={setInput2}
      />

      <Text style={styles.label}>Input 3:</Text>
      <TextInput
        style={styles.input}
        placeholder="Enter third input"
        value={input3}
        onChangeText={setInput3}
      />
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
