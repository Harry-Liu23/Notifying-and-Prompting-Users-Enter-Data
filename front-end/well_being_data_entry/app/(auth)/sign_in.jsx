import React, { useState } from 'react';
import { View, Text, TextInput, Button, Alert } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';

const SignIn = ({ navigation }) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleSignIn = () => {
    fetch('http://your-backend-server-url/api/auth/signin', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        email: email,
        password: password,
      }),
    })
      .then((response) => response.json())
      .then(async (data) => {
        if (data.success) {
          Alert.alert('Sign-in successful!');
          // Store JWT Token
          await AsyncStorage.setItem('token', data.token);
          // Navigate to home or dashboard screen
          navigation.navigate('Home');
        } else {
          Alert.alert('Sign-in failed', data.message);
        }
      })
      .catch((error) => {
        Alert.alert('Error', 'An error occurred during sign-in.');
      });
  };

  return (
    <View style={{ padding: 20 }}>
      <Text>Sign In</Text>
      <TextInput
        placeholder="Email"
        value={email}
        onChangeText={(text) => setEmail(text)}
        style={{ marginBottom: 10, padding: 10, borderWidth: 1 }}
      />
      <TextInput
        placeholder="Password"
        value={password}
        onChangeText={(text) => setPassword(text)}
        secureTextEntry
        style={{ marginBottom: 10, padding: 10, borderWidth: 1 }}
      />
      <Button title="Sign In" onPress={handleSignIn} />
    </View>
  );
};

export default SignIn;
