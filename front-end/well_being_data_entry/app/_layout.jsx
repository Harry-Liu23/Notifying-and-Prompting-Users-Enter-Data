import { StyleSheet, Text, View } from 'react-native'
import {Slot,Stack,SplashScreen} from 'expo-router'
import {useFonts} from 'expo-font'
import {useEffect} from 'react'

const RootLayout = () => {
  return (
    <>
        <Stack>
            <Stack.Screen name = "index"options={{headerShown:true}}/>
        </Stack>
    </>
  )
}

export default RootLayout