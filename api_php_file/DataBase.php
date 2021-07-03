<?php
require "DataBaseConfig.php";

class songIdAndsongPhotowithName {

    public $songId;
    public $songName;
    public $songPhoto;

    function setSongId ($songId) {
        $this->songId = $songId;
    }

    function setSongName ($songName) {
        $this->songName = $songName;
    }

    function setSongPhoto ($songPhoto) {
        $this->songPhoto = $songPhoto;
    }



}

class dataForRecyclerView {

    public $name;
    public $array = array();

    function setName($name) {
        $this->name = $name;
    }

    function setSongId($songId) {
        $this->songId = $songId;
    }

    function getName() {
        return $this->name;
    }

}

class dataForUpperSlide {

    public $songId;
    public $longSongPhoto;
    public $songPhoto;
    public $songName;

    function setSongId($songId) {
        $this->songId = $songId;
    }

    function setLongSongPhoto($longSongPhoto) {
        $this->longSongPhoto = $longSongPhoto;
    }

    function setSongPhoto($songPhoto) {
        $this->songPhoto = $songPhoto;
    }
    function setSongName($songName) {
        $this->songName = $songName;
    }


}

class DataBase
{
    public $connect;
    public $data;
    private $sql;
    protected $servername;
    protected $username;
    protected $password;
    protected $databasename;

    public function __construct()
    {
        $this->connect = null;
        $this->data = null;
        $this->sql = null;
        $dbc = new DataBaseConfig();
        $this->servername = $dbc->servername;
        $this->username = $dbc->username;
        $this->password = $dbc->password;
        $this->databasename = $dbc->databasename;
    }

    function dbConnect()
    {
        $this->connect = mysqli_connect($this->servername, $this->username, $this->password, $this->databasename);
        return $this->connect;
    }

    function prepareData($data)
    {
        return mysqli_real_escape_string($this->connect, stripslashes(htmlspecialchars($data)));
    }

    function getDataForUpperSlide($playlistId)
    {
        $playlistId = $this->prepareData($playlistId);
        $this->sql = "SELECT songId, longSongPhoto, songPhoto, songName FROM playlists p JOIN songs s ON (p.Song1 = s.songId || p.Song2 = s.songId || p.Song3 = s.songId
                        || p.Song4 = s.songId || p.Song5 = s.songId || p.Song6 = s.songId || p.Song7 = s.songId ||
                        p.Song8 = s.songId || p.Song9 = s.songId || p.Song10 = s.songId) WHERE playlistId = 1";
        $result = mysqli_query($this->connect, $this->sql);

        $jsonArray = array();



        while ($row = mysqli_fetch_assoc($result)) {

            $dataForUpperSlide = new dataForUpperSlide();

            $dataForUpperSlide->songId = $row['songId'];
            $dataForUpperSlide->longSongPhoto = base64_encode($row['longSongPhoto']);
            $dataForUpperSlide->songPhoto = base64_encode($row['songPhoto']);
            $dataForUpperSlide->songName = $row['songName'];
            array_push($jsonArray, $dataForUpperSlide);

        }

        return json_encode($jsonArray);
    }



    function getDataForRecyclerView($playlistId){

        $playlistId = $this->prepareData($playlistId);
        $this->sql = "SELECT playlistName, songId, songPhoto, songName FROM playlists p JOIN songs s ON (p.Song1 = s.songId || p.Song2 = s.songId || p.Song3 = s.songId
                        || p.Song4 = s.songId || p.Song5 = s.songId || p.Song6 = s.songId || p.Song7 = s.songId ||
                        p.Song8 = s.songId || p.Song9 = s.songId || p.Song10 = s.songId) WHERE playlistId = $playlistId";
        $result = mysqli_query($this->connect, $this->sql);   
        $data = new dataForRecyclerView();
     
        while ($row = mysqli_fetch_assoc($result)) {
            $songIdAndSongPhotowithName = new songIdAndsongPhotowithName();

            $songIdAndSongPhotowithName->songPhoto = base64_encode($row['songPhoto']);
            $songIdAndSongPhotowithName->songId = $row['songId'];
            $songIdAndSongPhotowithName->songName = $row['songName'];

            $data->setName($row['playlistName']);
            array_push($data->array, $songIdAndSongPhotowithName);
            
        }
        
        return $data;
    }

    function signUp($table, $id, $email, $firstname, $fullname, $photourl)
    {
        $id = $this->prepareData($id);
        $firstname = $this->prepareData($firstname);
        $fullname = $this->prepareData($fullname);
        $email = $this->prepareData($email);
        $photourl = $this->prepareData($photourl);
        $this->sql =
            "INSERT INTO " . $table . " (id, email, firstname, fullname, photourl) VALUES ('" . $id . "','" . $email . "','" . $firstname . "','" . $fullname . "','" . $photourl . "')";
        if (mysqli_query($this->connect, $this->sql)) {
            return true;
        } else return false;
    }


    function getMp3($table,$songId){
        $table = $this->prepareData($table);
        $songId = $this->prepareData($songId);

        $this->sql = "SELECT song FROM songs WHERE songId = $songId";
        $result = mysqli_query($this->connect, $this->sql);   

        $row = mysqli_fetch_assoc($result);
        $song = base64_encode($row['song']);
        
        return json_encode($song);
    }

    function getArtistId($songId) {
        $songId = $this->prepareData($songId);

        $this->sql = "SELECT artistName FROM songs s JOIN artists a ON (s.artistId1 = a.artistId || s.artistId2 = a.artistId || s.artistId3 = a.artistId || s.artistId4 = a.artistId || s.artistId5 = a.artistId) WHERE songId = $songId";
        $result = mysqli_query($this->connect, $this->sql);  

        $artistIdArray = array();

        while ($row = mysqli_fetch_assoc($result)) {
            array_push($artistIdArray, $row['artistName']);            
        }

        return $artistIdArray;

    }


}

?>
