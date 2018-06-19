<?php

$servername = "********";
$username = "********";
$password = "********";
$dbname = "********";

$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$content = file_get_contents("php://input");
$decoded = json_decode($content, true);

$login = $decoded["LOGIN"];
$password = $decoded["HASLO"];

$sql = "SELECT COUNT(*) AS total
    FROM UZYTKOWNIK
    WHERE LOGIN = '$login' AND HASLO = '$password';";

$query = mysqli_query($conn, $sql);

if ($rekord = mysqli_fetch_array($query)) {
    if ($rekord[0] > 0) {
        $out = array(
            "success" => "true",
        );
    }
    else {
        $out = array(
            "success" => "false",
        );
    }
}

echo json_encode($out);

$conn->close();

?>
