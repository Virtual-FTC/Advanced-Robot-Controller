package com.example.webrosrobotcontroller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActionMenuView;

import android.content.Intent;
import android.os.Bundle;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.qualcomm.robotcore.hardware.DcMotorMaster;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorImpl;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Enumeration;

import dalvik.system.DexFile;

public class MainActivity extends AppCompatActivity {
    private OpMode opMode;
    private boolean canUpdateGamepad = false;
    ArrayList<String> allClasses = new ArrayList<>();
    Spinner opModeSelector;
    Boolean selectedProgramIsLinearOpMode = null;
    Thread opModeThread;
    Thread gamepadCheckThread;
    ProgressBar progressBar;
    String activeConfigurationName = "";
    TextView telemetryOutputTextField;
    boolean canCheckForGamepad = false;
    Thread UnityUDPSendThread;
    Thread UnityUDPReceiveThread;
    String UnityUdpIpAddress = "35.197.110.179";
    long previousReceiveTime = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        canCheckForGamepad = true;
        DcMotorImpl.filesDir = this.getFilesDir();
        launchGamepadThread();
        populateClassSelector();
        progressBar = findViewById(R.id.loadingSign);
        progressBar.setVisibility(View.INVISIBLE);

        telemetryOutputTextField = findViewById(R.id.telemetryTextView);

        ActionMenuView bottomBar = findViewById(R.id.toolbar_bottom);

        Menu topMenu = bottomBar.getMenu();

        getMenuInflater().inflate(R.menu.menu, topMenu);

        new Thread(() -> {
            while(true) {
                try {
                    Thread.sleep(1000);
                    String yourFilePath = MainActivity.this.getFilesDir() + "/" + "activeConfig.txt";
                    File file = new File(yourFilePath);
                    FileInputStream fin = new FileInputStream(file);
                    int c;
                    String temp = "";
                    while ((c = fin.read()) != -1) {
                        temp = temp + (char) c;
                    }
                    fin.close();
                    activeConfigurationName = temp;


                    StringBuilder sb = new StringBuilder();
                    FileInputStream fis = new FileInputStream(new File(getFilesDir() + "/" + temp + ".txt"));
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader buff = new BufferedReader(isr);
                    String line;
                    while ((line = buff.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    fis.close();

                    DcMotorImpl.activeConfigContent = sb.toString();

                } catch (Exception e) {
                    e.printStackTrace();
                    activeConfigurationName = "No Config Set";
                }

                runOnUiThread(() -> {
                    TextView configTextView = findViewById(R.id.activeConfigName);
                    configTextView.setText(activeConfigurationName);
                });
            }
        }).start();

        topMenu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                canCheckForGamepad = false;
                Intent intent = new Intent(MainActivity.this, ConfigurationActivity.class);
                intent.putExtra("ACTIVE_CONFIGURATION_NAME", activeConfigurationName);
                startActivity(intent);
                return onOptionsItemSelected(item);
            }
        });

        final Button initStartButton = (Button) findViewById(R.id.initStartButton);
        initStartButton.setTag(0);
        initStartButton.setText("INIT");
        initStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int status = (Integer) v.getTag();
                if (status == 0) {
                    try {
                        Class opModeClass = Class.forName("org.firstinspires.ftc.teamcode." + opModeSelector.getSelectedItem().toString());//opModeSelector.getSelectedItem().toString().getClass();
                        opMode = (OpMode) opModeClass.newInstance();
                        initStartButton.setEnabled(false);
                        initOpModeThread();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    /**Unity RX and TX thread init**/

                    UnityUDPReceiveThread = new Thread(() -> {
                        try {
                            int port = 9051;
                            DatagramSocket socket = new DatagramSocket();
                            socket.connect(InetAddress.getByName(UnityUdpIpAddress), port);
                            String message = "hello";
                            socket.send(new DatagramPacket(message.getBytes(), message.length()));
                            while (true) {
                                try {
                                    byte[] buffer = new byte[1024];
                                    DatagramPacket response = new DatagramPacket(buffer, buffer.length);
                                    socket.receive(response);
                                    String responseText = new String(buffer, 0, response.getLength());

                                    JSONObject jsonObject = new JSONObject(responseText);
                                    DcMotorMaster.motorImpl1.encoderPosition = jsonObject.getDouble("motor1");
                                    DcMotorMaster.motorImpl2.encoderPosition = jsonObject.getDouble("motor2");
                                    DcMotorMaster.motorImpl3.encoderPosition = jsonObject.getDouble("motor3");
                                    DcMotorMaster.motorImpl4.encoderPosition = jsonObject.getDouble("motor4");
                                    DcMotorMaster.motorImpl5.encoderPosition = jsonObject.getDouble("motor5");
                                    DcMotorMaster.motorImpl6.encoderPosition = jsonObject.getDouble("motor6");
                                    DcMotorMaster.motorImpl7.encoderPosition = jsonObject.getDouble("motor7");
                                    DcMotorMaster.motorImpl8.encoderPosition = jsonObject.getDouble("motor8");

                                    if (previousReceiveTime != -1) {
                                        runOnUiThread(() -> {
                                            TextView fpsTextView = findViewById(R.id.fpsNumber);
                                            String fps = "" + Math.round(1000000000.0/(System.nanoTime() - previousReceiveTime));
                                            fpsTextView.setText(fps);
                                        });
                                    }
                                    previousReceiveTime = System.nanoTime();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    });

                    UnityUDPSendThread = new Thread(() -> {
                        try {
                            int port = 9050;
                            DatagramSocket socket = new DatagramSocket();
                            socket.connect(InetAddress.getByName(UnityUdpIpAddress), port);
                            socket.send(new DatagramPacket("reset".getBytes(), "reset".length()));
                            while (true) {
                                Thread.sleep(30);
                                try {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("motor1", DcMotorMaster.motorImpl1.power);
                                    jsonObject.put("motor2", DcMotorMaster.motorImpl2.power);
                                    jsonObject.put("motor3", DcMotorMaster.motorImpl3.power);
                                    jsonObject.put("motor4", DcMotorMaster.motorImpl4.power);
                                    jsonObject.put("motor5", DcMotorMaster.motorImpl5.power);
                                    jsonObject.put("motor6", DcMotorMaster.motorImpl6.power);
                                    jsonObject.put("motor7", DcMotorMaster.motorImpl7.power);
                                    jsonObject.put("motor8", DcMotorMaster.motorImpl8.power);
                                    String message = jsonObject.toString();
                                    socket.send(new DatagramPacket(message.getBytes(), message.length()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

                    initStartButton.setText("START");
                    v.setTag(1); //pause
//                    } else {
//                        Toast.makeText(MainActivity.this, "There is no IP Address to connect to. Please enter an IP address on the top.", Toast.LENGTH_LONG).show();
//                    }

                } else if (status == 1) {
                    initStartButton.setText("STOP");
                    UnityUDPReceiveThread.start();
                    UnityUDPSendThread.start();
                    v.setTag(2); //pause

                    launchOpModeThread(selectedProgramIsLinearOpMode);
                } else if (status == 2) {
                    opMode.stop();
                    opModeThread.interrupt();
                    opModeThread.interrupt();
                    initStartButton.setText("INIT");
                    v.setTag(0); //pause
                    selectedProgramIsLinearOpMode = null;
                    opModeThread = null;
                    DcMotorMaster.motorImpl1.power = 0.0;
                    DcMotorMaster.motorImpl2.power = 0.0;
                    DcMotorMaster.motorImpl3.power = 0.0;
                    DcMotorMaster.motorImpl4.power = 0.0;
                    DcMotorMaster.motorImpl5.power = 0.0;
                    DcMotorMaster.motorImpl6.power = 0.0;
                    DcMotorMaster.motorImpl7.power = 0.0;
                    DcMotorMaster.motorImpl8.power = 0.0;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    UnityUDPSendThread.interrupt();
                    UnityUDPReceiveThread.interrupt();
                }

            }
        });
    }

    private String getConfigurationFromYAMLFile() {
        try {
            StringBuilder sb = new StringBuilder();
            FileInputStream fis = new FileInputStream(new File(getFilesDir() + "/activeConfiguration.txt")); //actual file name is "activeConfiguration.txt". This line intentionally crashes and it is forced to go to the catch. ONLY FOR TESTING THE FORMAT OF SENDING TO ROS
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader buff = new BufferedReader(isr);

            String line;
            while ((line = buff.readLine()) != null) {
                sb.append(line);
            }

            fis.close();

            System.out.println(sb.toString());
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "[{\"frc\": \"frontLeft\", \"sim\": \"motor1\"},{\"frc\": \"frontRight\", \"sim\": \"motor2\"},{\"frc\": \"backLeft\", \"sim\": \"motor3\"},{\"frc\": \"backRight\", \"sim\": \"motor4\"},{\"frc\": \"intake\", \"sim\": \"motor5\"},{\"frc\": \"hopper\", \"sim\": \"motor6\"},{\"frc\": \"leftShooter\", \"sim\": \"motor7\"},{\"frc\": \"rightShooter\", \"sim\": \"motor8\"}]";
        }
    }

    private void launchGamepadThread() {
        gamepadCheckThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (canCheckForGamepad) {
                    checkForGamepad();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        gamepadCheckThread.setPriority(Thread.MAX_PRIORITY);
        gamepadCheckThread.start();
    }

    private void initOpModeThread() {
        progressBar.setVisibility(View.VISIBLE);
        Toast.makeText(MainActivity.this, "Initializing... Please wait", Toast.LENGTH_LONG).show();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    LinearOpMode linearOpMode = ((LinearOpMode) opMode);
                    selectedProgramIsLinearOpMode = true;
                    canUpdateGamepad = true;
                    enableProgramStart();
                } catch (Exception ignore) {
                    opMode.gamepad1 = new Gamepad();
                    try {
                        opMode.init();
                        canUpdateGamepad = true;
                        selectedProgramIsLinearOpMode = false;
                        enableProgramStart();
                    } catch (Exception e) {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(MainActivity.this, "Unable to connect to VM", Toast.LENGTH_SHORT).show();
                                Button initStartButton = (Button) findViewById(R.id.initStartButton);
                                initStartButton.setEnabled(true);
                                initStartButton.setTag(0);
                                initStartButton.setText("INIT");
                                progressBar.setVisibility(View.INVISIBLE);
                                canUpdateGamepad = false;
                                selectedProgramIsLinearOpMode = null;
                            }
                        });
                    }

                }
            }
        });
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }

    private void enableProgramStart() {
        this.runOnUiThread(new Runnable() {
            public void run() {
                Button initStartButton = (Button) findViewById(R.id.initStartButton);
                initStartButton.setEnabled(true);
                Toast.makeText(MainActivity.this, "You can START your program", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void populateClassSelector() {
        opModeSelector = (Spinner) findViewById(R.id.classSelector);
        try {
            String packageCodePath = getPackageCodePath();
            DexFile df = new DexFile(packageCodePath);
            for (Enumeration<String> iter = df.entries(); iter.hasMoreElements(); ) {
                String className = iter.nextElement();
                if (className.contains("org.firstinspires.ftc.teamcode")) {
                    allClasses.add(className.substring(className.lastIndexOf(".") + 1));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        ArrayList<String> classesWithValidAnnotations = new ArrayList<>();
//        for (int i = 0; i < allClasses.size(); i++) {
//            try {
//                Class classToCheck = allClasses.get(i).getClass();
//                if (classToCheck.isAnnotationPresent(TeleOp.class) || classToCheck.isAnnotationPresent(Autonomous.class)) {
//                    classesWithValidAnnotations.add(allClasses.get(i));
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, allClasses);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        opModeSelector.setAdapter(adapter);

    }

    private void launchOpModeThread(boolean isSeletedProgramLinearOpMode) {
        if (isSeletedProgramLinearOpMode) {
            opModeThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    MainActivity.this.wait(2500); //wait for 2.5 seconds to run any init code and establish web socket communication
                    try {
                        ((LinearOpMode) opMode).runOpMode();
                        MainActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                final Button initStartButton = (Button) findViewById(R.id.initStartButton);
                                initStartButton.setTag(0);
                                initStartButton.setText("INIT");
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (Exception ignore) {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(MainActivity.this, "Unable to connect to VM", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        } else {
            opModeThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    MainActivity.this.wait(2500); //wait for 2.5 seconds to run any init code and establish web socket communication
                    while (opModeThread != null) {
                        try {
                            opMode.loop();
                        } catch (Exception ignore) {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    canUpdateGamepad = false;
                                    selectedProgramIsLinearOpMode = null;
                                    opModeThread = null;
                                    Toast.makeText(MainActivity.this, "Connection Failed. Please try again.", Toast.LENGTH_SHORT).show();
                                    final Button initStartButton = (Button) findViewById(R.id.initStartButton);
                                    initStartButton.setTag(0);
                                    initStartButton.setText("INIT");
                                }
                            });
                        }
                    }
                }
            });
        }
        opModeThread.setPriority(Thread.MAX_PRIORITY);
        opModeThread.start();
    }

    private void wait(int ms) {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() < startTime + ms) {
            //wait for time to be over
        }
    }

    private void checkForGamepad() {
        int[] deviceIds = InputDevice.getDeviceIds();
        for (int deviceId : deviceIds) {
            InputDevice dev = InputDevice.getDevice(deviceId);
            int sources = dev.getSources();

            // Verify that the device has gamepad buttons, control sticks, or both.
            if (((sources & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD)
                    || ((sources & InputDevice.SOURCE_JOYSTICK)
                    == InputDevice.SOURCE_JOYSTICK)) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        ImageView gamepadImage = (ImageView) findViewById(R.id.gamepadImage);
                        gamepadImage.setVisibility(View.VISIBLE);
                    }
                });
                return;
            } else {
                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        ImageView gamepadImage = (ImageView) findViewById(R.id.gamepadImage);
                        gamepadImage.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((event.getSource() & InputDevice.SOURCE_GAMEPAD)
                == InputDevice.SOURCE_GAMEPAD) {
            if (event.getRepeatCount() == 0) {
                switch (keyCode) {
                    default:
                        if (canUpdateGamepad) {
                            if (keyCode == KeyEvent.KEYCODE_BUTTON_A) {
                                opMode.gamepad1.a = true;
                            } else if (keyCode == KeyEvent.KEYCODE_BUTTON_B) {
                                opMode.gamepad1.b = true;
                            } else if (keyCode == KeyEvent.KEYCODE_BUTTON_X) {
                                opMode.gamepad1.x = true;
                            } else if (keyCode == KeyEvent.KEYCODE_BUTTON_Y) {
                                opMode.gamepad1.y = true;
                            }
                            return true;
                        }

                        break;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if ((event.getSource() & InputDevice.SOURCE_GAMEPAD)
                == InputDevice.SOURCE_GAMEPAD) {
            if (event.getRepeatCount() == 0) {
                switch (keyCode) {
                    default:
                        if (canUpdateGamepad) {
                            if (keyCode == KeyEvent.KEYCODE_BUTTON_A) {
                                opMode.gamepad1.a = false;
                            } else if (keyCode == KeyEvent.KEYCODE_BUTTON_B) {
                                opMode.gamepad1.b = false;
                            } else if (keyCode == KeyEvent.KEYCODE_BUTTON_X) {
                                opMode.gamepad1.x = false;
                            } else if (keyCode == KeyEvent.KEYCODE_BUTTON_Y) {
                                opMode.gamepad1.y = false;
                            }
                            return true;
                        }

                        break;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * JOYSTICK CONTROLS FROM GAMEPAD
     */

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {

        // Check that the event came from a game controller
        if ((event.getSource() & InputDevice.SOURCE_JOYSTICK) ==
                InputDevice.SOURCE_JOYSTICK &&
                event.getAction() == MotionEvent.ACTION_MOVE) {

            // Process all historical movement samples in the batch
            final int historySize = event.getHistorySize();

            // Process the movements starting from the
            // earliest historical position in the batch
            for (int i = 0; i < historySize; i++) {
                // Process the event at historical position i
                processJoystickInput(event, i);
            }

            // Process the current movement sample in the batch (position -1)
            processJoystickInput(event, -1);
            return true;
        }
        return super.onGenericMotionEvent(event);
    }

    private static float getCenteredAxis(MotionEvent event, InputDevice device, int axis, int historyPos) {
        final InputDevice.MotionRange range =
                device.getMotionRange(axis, event.getSource());

        // A joystick at rest does not always report an absolute position of
        // (0,0). Use the getFlat() method to determine the range of values
        // bounding the joystick axis center.
        if (range != null) {
            final float flat = range.getFlat();
            final float value =
                    historyPos < 0 ? event.getAxisValue(axis) :
                            event.getHistoricalAxisValue(axis, historyPos);

            // Ignore axis values that are within the 'flat' region of the
            // joystick axis center.
            if (Math.abs(value) > flat) {
                return value;
            }
        }
        return 0;
    }

    private void processJoystickInput(MotionEvent event, int historyPos) {

        InputDevice inputDevice = event.getDevice();

        // Calculate the horizontal distance to move by
        // using the input value from one of these physical controls:
        // the left control stick, hat axis, or the right control stick.
        float x = getCenteredAxis(event, inputDevice,
                MotionEvent.AXIS_X, historyPos);
        float rx = getCenteredAxis(event, inputDevice,
                MotionEvent.AXIS_Z, historyPos);
        if (x == 0) {
            x = getCenteredAxis(event, inputDevice,
                    MotionEvent.AXIS_HAT_X, historyPos);
        }
        if (x == 0) {
            x = getCenteredAxis(event, inputDevice,
                    MotionEvent.AXIS_Z, historyPos);
        }

        // Calculate the vertical distance to move by
        // using the input value from one of these physical controls:
        // the left control stick, hat switch, or the right control stick.
        float y = getCenteredAxis(event, inputDevice,
                MotionEvent.AXIS_Y, historyPos);
        float ry = getCenteredAxis(event, inputDevice,
                MotionEvent.AXIS_RZ, historyPos);
        if (y == 0) {
            y = getCenteredAxis(event, inputDevice,
                    MotionEvent.AXIS_HAT_Y, historyPos);
        }
        if (y == 0) {
            y = getCenteredAxis(event, inputDevice,
                    MotionEvent.AXIS_RZ, historyPos);
        }

        telemetryOutputTextField.setText("x: " + Math.round(x * 100.0) / 100.0 +
                "\ny: " + Math.round(y * 100.0) / 100.0 +
                "\nrx: " + Math.round(rx * 100.0) / 100.0 +
                "\nry: " + Math.round(ry * 100.0) / 100.0 +
                "\nFL: " + (Math.hypot(Math.round(x * 100.0) / 100.0, -Math.round(y * 100.0) / 100.0) * Math.cos(Math.atan2(Math.round(y * 100.0) / 100.0, -Math.round(x * 100.0) / 100.0) - Math.PI / 4) - Math.round(rx * 100.0) / 100.0 / 2) +
                "\nFR: " + (Math.hypot(Math.round(x * 100.0) / 100.0, -Math.round(y * 100.0) / 100.0) * Math.sin(Math.atan2(Math.round(y * 100.0) / 100.0, -Math.round(x * 100.0) / 100.0) - Math.PI / 4) + Math.round(rx * 100.0) / 100.0 / 2) +
                "\nBL: " + (Math.hypot(Math.round(x * 100.0) / 100.0, -Math.round(y * 100.0) / 100.0) * Math.sin(Math.atan2(Math.round(y * 100.0) / 100.0, -Math.round(x * 100.0) / 100.0) - Math.PI / 4) - Math.round(rx * 100.0) / 100.0 / 2) +
                "\nBR: " + (Math.hypot(Math.round(x * 100.0) / 100.0, -Math.round(y * 100.0) / 100.0) * Math.cos(Math.atan2(Math.round(y * 100.0) / 100.0, -Math.round(x * 100.0) / 100.0) - Math.PI / 4) + Math.round(rx * 100.0) / 100.0 / 2));

        if (canUpdateGamepad) {
            opMode.gamepad1.left_stick_x = (float) (Math.round(x * 100.0) / 100.0);
            opMode.gamepad1.left_stick_y = (float) (Math.round(y * 100.0) / 100.0);
            opMode.gamepad1.right_stick_x = (float) (Math.round(rx * 100.0) / 100.0);
            opMode.gamepad1.right_stick_y = (float) (Math.round(ry * 100.0) / 100.0);
        } else {
            System.out.println("x: " + x + ", y: " + y + ", ");
        }
    }
}