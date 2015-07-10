# gifPlayer
<h1>Java GIF player and downloader</h1>
Java GIF player. Useful for playing a folder containing many (100+) gifs. File system buffer is set to 10 files at a time. You can change the fps of playback, loop a gif or shuffle.

<h2>Java gif player updated to v1.1 now featuring an integrated gif downloader.</h2>  You like gifs but don’t have time to find and download them? No problem! Click ‘Get GIFs’, select a folder, add keywords (you get around 20 gifs per keyword) and click download. After the download is finished (the process is currently kinda CPU demanding and may take some time), click done and the folder will be automatically loaded. Finally just click play and enjoy :). The downloader downloads gifs from Tumblr, NSFW content does not work as Yahoo changed Tumblr’s NSFW policy.

<h2>Update log</h2>
<h4>Drag and drop support - 10 July 2015</h4>
 - Drag and drop support added. Just drag gif files and click play. Haldned by  TLTHandler.java, minor modifications to  folderSelector.java
 - Minor optimization changes (buffer set to 2 files)
 - Console class commit (a full console will be available within the next version).
