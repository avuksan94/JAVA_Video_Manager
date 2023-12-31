USE [master]
GO
/****** Object:  Database [JMovies]    Script Date: 8/13/2023 11:19:04 AM ******/
CREATE DATABASE [JMovies]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'JMovies', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL14.MSSQLSERVER\MSSQL\DATA\JMovies.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'JMovies_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL14.MSSQLSERVER\MSSQL\DATA\JMovies_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
GO
ALTER DATABASE [JMovies] SET COMPATIBILITY_LEVEL = 140
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [JMovies].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [JMovies] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [JMovies] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [JMovies] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [JMovies] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [JMovies] SET ARITHABORT OFF 
GO
ALTER DATABASE [JMovies] SET AUTO_CLOSE ON 
GO
ALTER DATABASE [JMovies] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [JMovies] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [JMovies] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [JMovies] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [JMovies] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [JMovies] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [JMovies] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [JMovies] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [JMovies] SET  ENABLE_BROKER 
GO
ALTER DATABASE [JMovies] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [JMovies] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [JMovies] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [JMovies] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [JMovies] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [JMovies] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [JMovies] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [JMovies] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [JMovies] SET  MULTI_USER 
GO
ALTER DATABASE [JMovies] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [JMovies] SET DB_CHAINING OFF 
GO
ALTER DATABASE [JMovies] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [JMovies] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [JMovies] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [JMovies] SET QUERY_STORE = OFF
GO
USE [JMovies]
GO
/****** Object:  Table [dbo].[Actor]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Actor](
	[ActorID] [int] IDENTITY(1,1) NOT NULL,
	[PersonID] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[ActorID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Director]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Director](
	[DirectorID] [int] IDENTITY(1,1) NOT NULL,
	[PersonID] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[DirectorID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Movie]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Movie](
	[MovieID] [int] IDENTITY(1,1) NOT NULL,
	[Title] [nvarchar](255) NULL,
	[Description] [nvarchar](2048) NULL,
	[ReleaseYear] [int] NULL,
	[Genre] [nvarchar](255) NULL,
	[AddedAt] [datetime] NULL,
	[Duration] [int] NULL,
	[Poster] [nvarchar](1024) NULL,
PRIMARY KEY CLUSTERED 
(
	[MovieID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[MovieActor]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[MovieActor](
	[MovieID] [int] NOT NULL,
	[ActorID] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[MovieID] ASC,
	[ActorID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[MovieDirector]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[MovieDirector](
	[MovieID] [int] NOT NULL,
	[DirectorID] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[MovieID] ASC,
	[DirectorID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Person]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Person](
	[PersonID] [int] IDENTITY(1,1) NOT NULL,
	[FirstName] [nvarchar](255) NULL,
	[LastName] [nvarchar](255) NULL,
	[ImageURL] [varchar](2048) NULL,
	[Age] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[PersonID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[User]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[User](
	[UserID] [int] IDENTITY(1,1) NOT NULL,
	[Username] [nvarchar](255) NULL,
	[PasswordHash] [nvarchar](255) NULL,
	[Email] [nvarchar](255) NULL,
	[Role] [nvarchar](255) NULL,
	[PersonID] [int] NULL,
	[CreatedAt] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[UserID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
ALTER TABLE [dbo].[Movie] ADD  DEFAULT (getdate()) FOR [AddedAt]
GO
ALTER TABLE [dbo].[User] ADD  DEFAULT (getdate()) FOR [CreatedAt]
GO
ALTER TABLE [dbo].[Actor]  WITH CHECK ADD FOREIGN KEY([PersonID])
REFERENCES [dbo].[Person] ([PersonID])
GO
ALTER TABLE [dbo].[Director]  WITH CHECK ADD FOREIGN KEY([PersonID])
REFERENCES [dbo].[Person] ([PersonID])
GO
ALTER TABLE [dbo].[MovieActor]  WITH CHECK ADD FOREIGN KEY([ActorID])
REFERENCES [dbo].[Actor] ([ActorID])
GO
ALTER TABLE [dbo].[MovieActor]  WITH CHECK ADD FOREIGN KEY([MovieID])
REFERENCES [dbo].[Movie] ([MovieID])
GO
ALTER TABLE [dbo].[MovieDirector]  WITH CHECK ADD FOREIGN KEY([DirectorID])
REFERENCES [dbo].[Director] ([DirectorID])
GO
ALTER TABLE [dbo].[MovieDirector]  WITH CHECK ADD FOREIGN KEY([MovieID])
REFERENCES [dbo].[Movie] ([MovieID])
GO
ALTER TABLE [dbo].[User]  WITH CHECK ADD FOREIGN KEY([PersonID])
REFERENCES [dbo].[Person] ([PersonID])
GO
/****** Object:  StoredProcedure [dbo].[checkMovieActorRelation]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[checkMovieActorRelation] 
	@movieId INT, 
	@actorId INT
AS
BEGIN
    IF EXISTS (SELECT * FROM MovieActor WHERE MovieID = @movieId AND ActorID = @actorId)
        SELECT 1
    ELSE
        SELECT 0
END
GO
/****** Object:  StoredProcedure [dbo].[checkMovieDirectorRelation]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[checkMovieDirectorRelation] 
	@movieId INT, 
	@directorId INT
AS
BEGIN
    IF EXISTS (SELECT * FROM MovieDirector WHERE MovieID = @movieId AND DirectorID = @directorId)
        SELECT 1
    ELSE
        SELECT 0
END
GO
/****** Object:  StoredProcedure [dbo].[createActor]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[createActor]
	@ActorId INT OUTPUT,
	@PersonId INT
AS 
BEGIN 
	INSERT INTO Actor VALUES(@PersonId)
	SET @ActorId = SCOPE_IDENTITY()
END
GO
/****** Object:  StoredProcedure [dbo].[createDirector]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[createDirector]
	@DirectorId INT OUTPUT,
	@PersonId INT
AS 
BEGIN 
	INSERT INTO Director VALUES(@PersonId)
	SET @DirectorId = SCOPE_IDENTITY()
END
GO
/****** Object:  StoredProcedure [dbo].[createMovie]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[createMovie]
	@MovieId INT OUTPUT,
	@Title NVARCHAR(255),
	@Description NVARCHAR(2048),
	@ReleaseYear INT,
	@Genre NVARCHAR(255),
	@AddedAt datetime,
	@Duration INT,
	@Poster NVARCHAR(1024)
AS 
BEGIN 
	INSERT INTO [Movie] VALUES(@Title, @Description, @ReleaseYear, @Genre, @AddedAt,@Duration,@Poster)
	SET @MovieId = SCOPE_IDENTITY()
END
GO
/****** Object:  StoredProcedure [dbo].[createMovieActor]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[createMovieActor]
    @MovieID INT,
    @ActorID INT
AS
BEGIN
    INSERT INTO MovieActor (MovieID, ActorID)
    VALUES (@MovieID, @ActorID);
END
GO
/****** Object:  StoredProcedure [dbo].[createMovieDirector]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[createMovieDirector]
    @MovieID INT,
    @DirectorID INT
AS
BEGIN
    INSERT INTO MovieDirector (MovieID, DirectorID)
    VALUES (@MovieID, @DirectorID);
END
GO
/****** Object:  StoredProcedure [dbo].[createPerson]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[createPerson]
	@PersonId INT OUTPUT,
	@FirstName NVARCHAR(255),
	@Lastname NVARCHAR(255),
	@ImageURL NVARCHAR(2048),
	@Age INT
AS 
BEGIN 
	INSERT INTO Person VALUES(@FirstName, @Lastname, @ImageURL, @Age)
	SET @PersonId = SCOPE_IDENTITY()
END
GO
/****** Object:  StoredProcedure [dbo].[createUser]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[createUser]
	@UserId INT OUTPUT,
	@Username NVARCHAR(255),
	@PasswordHash NVARCHAR(255),
	@Email NVARCHAR(255),
	@Role NVARCHAR(255),
	@PersonId INT,
	@CreatedAt datetime
AS 
BEGIN 
	INSERT INTO [User] VALUES(@Username, @PasswordHash, @Email, @Role, @PersonId,@CreatedAt)
	SET @UserId = SCOPE_IDENTITY()
END
GO
/****** Object:  StoredProcedure [dbo].[deleteActor]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[deleteActor]
    @ActorId INT	 
AS 
BEGIN 

    DELETE FROM MovieActor
    WHERE ActorID = @ActorId;

    DELETE FROM Actor
    WHERE ActorID = @ActorId;

END
GO
/****** Object:  StoredProcedure [dbo].[deleteDirector]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[deleteDirector]
	@DirectorId INT	 
AS 
BEGIN 
	DELETE FROM MovieDirector
    WHERE DirectorID = @DirectorId;

    DELETE FROM Director
    WHERE DirectorID = @DirectorId;
END
GO
/****** Object:  StoredProcedure [dbo].[deleteMovie]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
Create PROCEDURE [dbo].[deleteMovie]
	@MovieId INT	 
AS 
BEGIN 
	DELETE  
	FROM 
		[Movie]
	WHERE 
		MovieID = @MovieId
END
GO
/****** Object:  StoredProcedure [dbo].[deleteMovieActor]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[deleteMovieActor]
    @MovieID INT,
    @ActorID INT
AS
BEGIN
    DELETE FROM MovieActor
    WHERE MovieID = @MovieID AND ActorID = @ActorID;
END
GO
/****** Object:  StoredProcedure [dbo].[deleteMovieDirector]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[deleteMovieDirector]
	@MovieID INT,
    @DirectorID INT
AS
BEGIN
    DELETE FROM MovieDirector
    WHERE MovieID = @MovieID AND DirectorID = @DirectorID;
END
GO
/****** Object:  StoredProcedure [dbo].[deletePerson]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[deletePerson]
	@PersonId INT	 
AS 
BEGIN 
	DELETE  
	FROM 
		Person
	WHERE 
		PersonID = @PersonId
END
GO
/****** Object:  StoredProcedure [dbo].[deleteUser]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[deleteUser]
	@UserId INT	 
AS 
BEGIN 
	DELETE  
	FROM 
		[User]
	WHERE 
		UserID = @UserId
END
GO
/****** Object:  StoredProcedure [dbo].[selectActor]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[selectActor]
	@ActorID INT
AS 
BEGIN 
	SELECT 
		* 
	FROM 
		[Actor]
	WHERE 
		ActorID = @ActorID
END
GO
/****** Object:  StoredProcedure [dbo].[selectActors]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[selectActors]
AS 
BEGIN 
	SELECT * FROM [Actor]
END
GO
/****** Object:  StoredProcedure [dbo].[selectActorsByMovie]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[selectActorsByMovie]
    @MovieID INT
AS
BEGIN
    SELECT act.*
    FROM Actor AS act
    INNER JOIN MovieActor AS ma ON act.ActorID = ma.ActorID
    WHERE ma.MovieID = @MovieID;
END
GO
/****** Object:  StoredProcedure [dbo].[selectDirector]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[selectDirector]
	@DirectorID INT
AS 
BEGIN 
	SELECT 
		* 
	FROM 
		Director
	WHERE 
		DirectorID = @DirectorID
END
GO
/****** Object:  StoredProcedure [dbo].[selectDirectors]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[selectDirectors]
AS 
BEGIN 
	SELECT * FROM Director
END
GO
/****** Object:  StoredProcedure [dbo].[selectDirectorsByMovie]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[selectDirectorsByMovie]
    @MovieID INT
AS
BEGIN
    SELECT direct.*
    FROM Director AS direct
    INNER JOIN MovieDirector AS md ON direct.DirectorID = md.DirectorID
    WHERE md.MovieID = @MovieID;
END
GO
/****** Object:  StoredProcedure [dbo].[selectMovie]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[selectMovie]
	@MovieID INT
AS 
BEGIN 
	SELECT 
		* 
	FROM 
		Movie
	WHERE 
		MovieID = @MovieID
END
GO
/****** Object:  StoredProcedure [dbo].[selectMovieActor]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[selectMovieActor]
	@MovieID INT
AS 
BEGIN 
	SELECT 
		* 
	FROM 
		MovieActor
	WHERE 
		MovieID = @MovieID
END
GO
/****** Object:  StoredProcedure [dbo].[selectMovieActors]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[selectMovieActors]
AS 
BEGIN 
	SELECT * FROM MovieActor
END
GO
/****** Object:  StoredProcedure [dbo].[selectMovies]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[selectMovies]
AS 
BEGIN 
	SELECT 
		* 
	FROM 
		Movie
END
GO
/****** Object:  StoredProcedure [dbo].[selectPerson]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[selectPerson]
	@PersonID INT
AS 
BEGIN 
	SELECT 
		* 
	FROM 
		Person
	WHERE 
		PersonID = @PersonID
END
GO
/****** Object:  StoredProcedure [dbo].[selectPersons]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[selectPersons]
AS 
BEGIN 
	SELECT * FROM Person
END
GO
/****** Object:  StoredProcedure [dbo].[selectUser]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[selectUser]
	@UserID INT
AS 
BEGIN 
	SELECT 
		* 
	FROM 
		[User]
	WHERE 
		UserID = @UserID
END
GO
/****** Object:  StoredProcedure [dbo].[selectUsers]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[selectUsers]
AS 
BEGIN 
	SELECT * FROM [User]
END
GO
/****** Object:  StoredProcedure [dbo].[updateMovie]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[updateMovie]
	@MovieId INT OUTPUT,
	@Title NVARCHAR(255),
	@Description NVARCHAR(2048),
	@ReleaseYear INT,
	@Genre NVARCHAR(255),
	@Duration INT,
	@Poster NVARCHAR(255)
AS 
BEGIN 
	UPDATE [Movie] SET 
		Title = @Title,
		[Description] = @Description,
		ReleaseYear = @ReleaseYear,
		Genre = @Genre,
		Duration = @Duration,
		Poster = @Poster
	WHERE 
		MovieId = @MovieId
END
GO
/****** Object:  StoredProcedure [dbo].[updatePerson]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[updatePerson]
	@PersonId INT,
	@FirstName NVARCHAR(255),
	@Lastname NVARCHAR(255),
	@ImageURL NVARCHAR(2048),
	@Age INT
	 
AS 
BEGIN 
	UPDATE Person SET 
		FirstName = @FirstName,
		Lastname = @Lastname,
		ImageURL = @ImageURL,
		Age = @Age		
	WHERE 
		PersonId = @PersonId
END
GO
/****** Object:  StoredProcedure [dbo].[updateUser]    Script Date: 8/13/2023 11:19:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[updateUser]
	@UserId INT OUTPUT,
	@Username NVARCHAR(255),
	@PasswordHash NVARCHAR(255),
	@Email NVARCHAR(255),
	@Role NVARCHAR(255)
AS 
BEGIN 
	UPDATE [User] SET 
		Username = @Username,
		PasswordHash = @PasswordHash,
		Email = @Email,
		[Role] = @Role
	WHERE 
		UserId = @UserId
END
GO
USE [master]
GO
ALTER DATABASE [JMovies] SET  READ_WRITE 
GO
