<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['id'])) {
    if ($db->dbConnect()) {
        $dbLongSongPhoto = $db->getMp3("songs",$_POST['id']);
        print($dbLongSongPhoto);
    } else echo "Error: Database connection";
} else echo "All fields are required";

?>