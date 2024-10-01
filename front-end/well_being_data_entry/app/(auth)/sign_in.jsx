import React, { useState } from 'react';
import { View, Text, TextInput, Button, Alert } from 'react-native';
import { tailwind } from 'tailwindcss-react-native';

const SignIn = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleSignIn = () => {
    // You would replace this with actual sign-in logic such as an API call
    if (email === '' || password === '') {
      Alert.alert('Error', 'Please fill in all fields');
    } else {
      // Simulate successful sign-in
      Alert.alert('Success', `Signed in as ${email}`);
    }
  };

  return (
    <View style={tailwind('flex-1 justify-center p-4 bg-white')}>
      <Text style={tailwind('text-2xl font-bold mb-6 text-center')}>Sign In</Text>

      <Text style={tailwind('text-lg mb-2')}>Email:</Text>
      <TextInput
        style={tailwind('h-10 border border-gray-400 px-2 mb-4 rounded')}
        placeholder="Enter your email"
        value={email}
        onChangeText={setEmail}
        keyboardType="email-address"
        autoCapitalize="none"
      />

      <Text style={tailwind('text-lg mb-2')}>Password:</Text>
      <TextInput
        style={tailwind('h-10 border border-gray-400 px-2 mb-4 rounded')}
        placeholder="Enter your password"
        value={password}
        onChangeText={setPassword}
        secureTextEntry
      />

      <Button title="Sign In" onPress={handleSignIn} />
    </View>
  );
};

export default SignIn;
