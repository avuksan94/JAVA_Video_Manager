/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package hr.algebra.view;

import hr.algebra.bll.blModels.ActorModel;
import hr.algebra.bll.blModels.DirectorModel;
import hr.algebra.bll.blModels.MovieModel;
import hr.algebra.command.AddActorCommand;
import hr.algebra.command.AddDirectorCommand;
import hr.algebra.command.CommandInvoker;
import hr.algebra.command.RemoveActorCommand;
import hr.algebra.command.RemoveDirectorCommand;
import hr.algebra.controller.UserMovieController;
import hr.algebra.dal.jbModels.ActorJB;
import hr.algebra.dal.jbModels.DirectorJB;
import hr.algebra.dal.jbModels.MovieArchiveJB;
import hr.algebra.dal.jbModels.MovieArchiveJB.Channel;
import hr.algebra.dal.jbModels.MovieJB;
import hr.algebra.dal.models.Actor;
import hr.algebra.dal.models.Director;
import hr.algebra.models.MovieTableModel;
import hr.algebra.transferable.ActorTransferable;
import hr.algebra.transferable.DirectorTransferable;
import hr.algebra.utilities.FileUtils;
import hr.algebra.utilities.IconUtils;
import hr.algebra.utilities.JAXBUtils;
import hr.algebra.utilities.MessageUtils;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.text.JTextComponent;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 *
 * @author antev
 */
public class ManageMoviesView extends javax.swing.JPanel {

    /**
     * Creates new form ManageMoviesView
     */
    private static final String DIR = "assets";
    private static final String FILENAME = "src/main/resources/movieArchive.xml";
    private CommandInvoker _commandInvoker;

    //FOR ACTORS DISPLAY
    private final Set<ActorModel> allActors = new TreeSet<>();
    private final Set<ActorModel> actors = new TreeSet<>();
    private final DefaultListModel<ActorModel> allActorsModel = new DefaultListModel<>();
    private final DefaultListModel<ActorModel> actorsModel = new DefaultListModel<>();

    //FOR DIRECTORS DISPLAY
    private final Set<DirectorModel> allDirectors = new TreeSet<>();
    private final Set<DirectorModel> directors = new TreeSet<>();
    private final DefaultListModel<DirectorModel> allDirectorsModel = new DefaultListModel<>();
    private final DefaultListModel<DirectorModel> directorsModel = new DefaultListModel<>();

    private StringBuilder errorMessages;

    private List<JTextComponent> validationFields;
    private List<JLabel> errorLabels;

    private MovieTableModel moviesTableModel;
    private MovieModel selectedModel;

    private UserMovieController userController;

    public ManageMoviesView() {
        initComponents();
    }

    private void init() {
        try {
            initModel();
            initValidation();
            hideErrors();
            initPoster();
            initController();
            initTable();
            initActorDragNDrop();
            initDirectorDragNDrop();
            initCommandInvoker();
        } catch (Exception ex) {
            Logger.getLogger(ManageMoviesView.class.getName()).log(Level.SEVERE, null, ex);
            MessageUtils.showErrorMessage("Unrecoverable error", "Cannot initiate the form");
            System.exit(1);
        }
    }
    
    private  void initPoster()
    {
        taPicturePath.setText("/assets/no_image.png");
    }

    private void initModel() {
        selectedModel = new MovieModel();
    }

    private void initCommandInvoker() {
        _commandInvoker = new CommandInvoker();
    }

    private void initValidation() {
        validationFields = Arrays.asList(tfTitle, taDescription, tfDuration, tfGenre, tfReleaseYear, tfAddedAt);
        errorLabels = Arrays.asList(lbErrorTitle, lbErrorDescription, lbErrorDuration, lbErrorGenre, lbErrorReleaseYear, lbErrorAddedAt,lbErrorPoster);
    }

    private void hideErrors() {
        errorLabels.forEach(e -> e.setVisible(false));
    }

    private void showErrors() {
        errorLabels.forEach(e -> e.setVisible(true));
    }

    private boolean checkIfMovieExists(String title) throws Exception {
        return userController.doesMovieExist(title);
    }

    private void initController() throws Exception {
        MovieModel movieModel = new MovieModel();
        userController = new UserMovieController(movieModel, this);
    }

    private void initTable() throws Exception {
        tbMovieModelTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbMovieModelTable.setAutoCreateRowSorter(true);
        tbMovieModelTable.setRowHeight(25);
        moviesTableModel = new MovieTableModel(userController.getMoviesForModel());
        tbMovieModelTable.setModel(moviesTableModel);
    }

    private void fillForm(MovieModel movieModel) throws Exception {
        if (movieModel.getPoster() != null && Files.exists(Paths.get(movieModel.getPoster()))) {
            taPicturePath.setText(movieModel.getPoster());
            setIcon(lbPosterIcon, new File(movieModel.getPoster()));
        }

        tfTitle.setText(movieModel.getTitle());
        taDescription.setText(movieModel.getDescription());

        String duration = Integer.toString(movieModel.getDuration());
        tfDuration.setText(duration);

        tfGenre.setText(movieModel.getGenre());

        String releaseYear = Integer.toString(movieModel.getReleaseYear());
        tfReleaseYear.setText(releaseYear);

        tfAddedAt.setText(movieModel.getAddedAt().format(MovieModel.DATE_FORMATTER));

        List<ActorModel> mvActors = userController.getActorsForMovie(movieModel);
        actors.addAll(mvActors);

        List<DirectorModel> mvDirectors = userController.getDirectorsForMovie(movieModel);
        directors.addAll(mvDirectors);

        loadActorsModel();
        loadDirectorsModel();
    }

    private void loadActorsAndDirectors() {
        try {

            allActors.clear();
            allDirectors.clear();

            List<ActorModel> actorsToAdd = userController.getActorsForModel();
            allActors.addAll(actorsToAdd);

            List<DirectorModel> directorsToAdd = userController.getDirectorsForModel();
            allDirectors.addAll(directorsToAdd);

            loadAllActorsModel();
            loadAllDirectorsModel();

        } catch (Exception ex) {
            MessageUtils.showErrorMessage("Error", "Unable to load movies");
            Logger.getLogger(ManageMoviesView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //LOADING AUTHORS
    private void loadAllActorsModel() {
        allActorsModel.clear();
        allActors.forEach(allActorsModel::addElement);
        lsAllActors.setModel(allActorsModel);
    }

    private void loadActorsModel() {
        actorsModel.clear();
        actors.forEach(actorsModel::addElement);
        lsActors.setModel(actorsModel);
    }

    //LOADING DIRECTORS
    private void loadAllDirectorsModel() {
        allDirectorsModel.clear();
        allDirectors.forEach(allDirectorsModel::addElement);
        lsAllDirectors.setModel(allDirectorsModel);
    }

    private void loadDirectorsModel() {
        directorsModel.clear();
        directors.forEach(directorsModel::addElement);
        lsDirectors.setModel(directorsModel);
    }

    //DRAG AND DROP IMPLEMENTATION
    private void initActorDragNDrop() {
        lsAllActors.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lsAllActors.setDragEnabled(true);
        lsAllActors.setTransferHandler(new ExportTransferHandler());

        lsActors.setDropMode(DropMode.ON);
        lsActors.setTransferHandler(new ImportTransferHandler());
    }

    private void initDirectorDragNDrop() {
        lsAllDirectors.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lsAllDirectors.setDragEnabled(true);
        lsAllDirectors.setTransferHandler(new ExportTransferHandler());

        lsDirectors.setDropMode(DropMode.ON);
        lsDirectors.setTransferHandler(new ImportTransferHandler());
    }

    private class ExportTransferHandler extends TransferHandler {

        @Override
        public int getSourceActions(JComponent c) {
            return COPY;
        }

        @Override
        public Transferable createTransferable(JComponent c) {
            if (c == lsAllActors) {
                return new ActorTransferable(lsAllActors.getSelectedValue());
            } else if (c == lsAllDirectors) {
                return new DirectorTransferable(lsAllDirectors.getSelectedValue());
            }
            return null;
        }
    }

    private class ImportTransferHandler extends TransferHandler {

        /* private ActorModel draggedActor;
        private DirectorModel draggedDirector;*/
        @Override
        public boolean canImport(TransferSupport support) {
            return support.isDataFlavorSupported(ActorTransferable.ACTOR_FLAVOR)
                    || support.isDataFlavorSupported(DirectorTransferable.DIRECTOR_FLAVOR);
        }

        @Override
        public boolean importData(TransferSupport support) {
            Transferable transferable = support.getTransferable();
            try {
                if (selectedModel != null) {

                    if (support.isDataFlavorSupported(ActorTransferable.ACTOR_FLAVOR)) {
                        ActorModel actor = (ActorModel) transferable.getTransferData(ActorTransferable.ACTOR_FLAVOR);
                        AddActorCommand addActorCommand = new AddActorCommand(actors, actor, selectedModel);
                        _commandInvoker.execute(addActorCommand);
                        loadActorsModel();
                        return true;
                    } else if (support.isDataFlavorSupported(DirectorTransferable.DIRECTOR_FLAVOR)) {
                        DirectorModel director = (DirectorModel) transferable.getTransferData(DirectorTransferable.DIRECTOR_FLAVOR);
                        AddDirectorCommand addDirectorCommand = new AddDirectorCommand(directors, director, selectedModel);
                        _commandInvoker.execute(addDirectorCommand);
                        loadDirectorsModel();
                        return true;
                    }
                }
                // Update the appropriate model here, and return true if successful
            } catch (UnsupportedFlavorException | IOException | SQLException ex) {
                Logger.getLogger(ManageMoviesView.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }
    }

    private void clearForm() {
        hideErrors();
        validationFields.forEach(e -> e.setText(""));
        //lbPosterIcon.setIcon(new javax.swing.ImageIcon("JMoviesManager\\src\\main\\resources\\assets\\no_image.png"));
        lbPosterIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/no_image.png")));
        taPicturePath.setText("/assets/no_image.png");
        selectedModel = null;
    }

    private boolean formValid() {
        hideErrors();
        boolean ok = true;

        // Create validator
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        int releaseYear = 0;
        int duration = 0;
        LocalDateTime date = null;

        String yearText = tfReleaseYear.getText();
        if (!yearText.isEmpty()) {
            try {
                releaseYear = Integer.parseInt(yearText);
                lbErrorReleaseYear.setVisible(false);
            } catch (NumberFormatException e) {
                lbErrorReleaseYear.setVisible(true);
            }
        } else {
            lbErrorReleaseYear.setVisible(true);
            lbErrorReleaseYear.setText("Year is invalid format");
        }

        String durationYext = tfDuration.getText();
        if (!durationYext.isEmpty()) {
            try {
                duration = Integer.parseInt(durationYext);
                lbErrorDuration.setVisible(false);
            } catch (NumberFormatException e) {
                lbErrorDuration.setVisible(true);
            }
        } else {
            lbErrorDuration.setVisible(true);
            lbErrorDuration.setText("Duration is invalid format");
        }

        String dateTimeText = tfAddedAt.getText();

        if (!dateTimeText.isEmpty()) {
            try {
                date = LocalDateTime.parse(dateTimeText);
                lbErrorAddedAt.setVisible(false);
            } catch (DateTimeParseException e) {
                lbErrorAddedAt.setVisible(true);
                lbErrorAddedAt.setText("Date-time is in an invalid format");
            }
        } else {
            lbErrorAddedAt.setVisible(true);
            lbErrorAddedAt.setText("Date-time cannot be empty");
        }

        selectedModel = new MovieModel(
                tfTitle.getText().trim(),
                taDescription.getText().trim(),
                releaseYear,
                tfGenre.getText().trim(),
                date,
                duration,
                taPicturePath.getText().trim(),
                new ArrayList<Actor>(),
                new ArrayList<Director>()
        );

        // Validate models
        Set<ConstraintViolation<MovieModel>> movieModelViolations = validator.validate(selectedModel);

        ok = validateMovieModel(movieModelViolations);

        return ok;
    }

    public boolean validateMovieModel(Set<ConstraintViolation<MovieModel>> mv) {
        boolean isValid = true;
        errorMessages = new StringBuilder();
        // If there are any violations, print the error messages
        if (!mv.isEmpty()) {
            isValid = false;
            mv.forEach(violation -> {
                String path = violation.getPropertyPath().toString();
                switch (path) {
                    case "title":
                        lbErrorTitle.setVisible(true);
                        lbErrorTitle.setText(violation.getMessage());
                        break;
                    case "description":
                        lbErrorDescription.setVisible(true);
                        lbErrorDescription.setText(violation.getMessage());
                        break;
                    case "duration":
                        lbErrorDuration.setVisible(true);
                        lbErrorDuration.setText(violation.getMessage());
                        break;
                    case "genre":
                        lbErrorGenre.setVisible(true);
                        lbErrorGenre.setText(violation.getMessage());
                        break;
                    case "releaseYear":
                        lbErrorReleaseYear.setVisible(true);
                        lbErrorReleaseYear.setText(violation.getMessage());
                        break;
                    case "addedAt":
                        lbErrorAddedAt.setVisible(true);
                        lbErrorAddedAt.setText(violation.getMessage());
                        break;
                    case "poster":
                        lbErrorPoster.setVisible(true);
                        lbErrorPoster.setText(violation.getMessage());
                        break;
                }
                errorMessages.append(violation.getMessage());
                errorMessages.append("\n");
                //System.out.println(violation.getMessage());
            });
        }

        return isValid;
    }

    private void undoAllCommands() {
        while (!_commandInvoker.isEmpty()) {
            _commandInvoker.undo();
        }
        _commandInvoker.clear();
    }

    private void executeAllCommands() {
        while (!_commandInvoker.isEmpty()) {
            _commandInvoker.executeNext();
        }
        _commandInvoker.clear();
    }

    private void showMovie() {
        //System.out.println(new File(".").getAbsolutePath());
        actors.clear();
        directors.clear();
        undoAllCommands();
        _commandInvoker.clear();
        clearForm();
        int selectedRow = tbMovieModelTable.getSelectedRow();
        int rowIndex = tbMovieModelTable.convertRowIndexToModel(selectedRow);
        int selectedMovieId = (int) moviesTableModel.getValueAt(rowIndex, 0);

        try {
            selectedModel = userController.getMovieForModel(selectedMovieId);
            fillForm(selectedModel);
        } catch (Exception ex) {
            Logger.getLogger(ManageUsersView.class.getName()).log(Level.SEVERE, null, ex);
            MessageUtils.showErrorMessage("Error", "Unable to show movie!");
        }
    }

    private void setIcon(JLabel label, File file) {
        try {
            label.setIcon(IconUtils.createIcon(file, label.getWidth(), label.getHeight()));
        } catch (IOException ex) {
            Logger.getLogger(ManageMoviesView.class.getName()).log(Level.SEVERE, null, ex);
            MessageUtils.showErrorMessage("Error", "Unable to set icon!");
        }
    }

    private String uploadPicture() throws IOException {
        String picturePath = taPicturePath.getText();
        String ext = picturePath.substring(picturePath.lastIndexOf("."));
        String pictureName = UUID.randomUUID() + ext;
        String localPicturePath = DIR + File.separator + pictureName;

        FileUtils.copy(picturePath, localPicturePath);
        return localPicturePath;
    }

    //JAX B
    private List<MovieJB> convertMovieToJAXB(List<MovieModel> moviesMods) {
        return moviesMods.stream().map(movieMod -> {
            List<DirectorJB> directorsJb = new ArrayList<>();
            List<ActorJB> actorsJb = new ArrayList<>();

            try {
                directorsJb = convertDirectorsToJAXB(userController.getDirectorsForMovie(movieMod));
                actorsJb = convertActorsToJAXB(userController.getActorsForMovie(movieMod));
            } catch (Exception ex) {
                Logger.getLogger(ManageMoviesView.class.getName()).log(Level.SEVERE, null, ex);
            }

            return new MovieJB(
                    movieMod.getTitle(),
                    movieMod.getAddedAt(),
                    movieMod.getDescription(),
                    directorsJb,
                    actorsJb,
                    movieMod.getDuration(),
                    movieMod.getReleaseYear(),
                    movieMod.getGenre(),
                    movieMod.getPoster()
            );
        }).collect(Collectors.toList());
    }

    private List<DirectorJB> convertDirectorsToJAXB(List<DirectorModel> directorModels) {
        return directorModels.stream()
                .map(model -> new DirectorJB(model.getFirstName(), model.getLastName()))
                .collect(Collectors.toList());
    }

    private List<ActorJB> convertActorsToJAXB(List<ActorModel> actorModels) {
        return actorModels.stream()
                .map(model -> new ActorJB(model.getFirstName(), model.getLastName()))
                .collect(Collectors.toList());
    }

    private void ifFileExists() throws IOException {
        Path filePath = Paths.get(FILENAME);
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tbMovieModelTable = new javax.swing.JTable();
        lbTitle = new javax.swing.JLabel();
        lbDescription = new javax.swing.JLabel();
        lbDuration = new javax.swing.JLabel();
        lbGenre = new javax.swing.JLabel();
        lbReleaseYear = new javax.swing.JLabel();
        lbAddedAt = new javax.swing.JLabel();
        tfTitle = new javax.swing.JTextField();
        tfDuration = new javax.swing.JTextField();
        tfGenre = new javax.swing.JTextField();
        tfReleaseYear = new javax.swing.JTextField();
        tfAddedAt = new javax.swing.JTextField();
        lbErrorTitle = new javax.swing.JLabel();
        lbErrorDescription = new javax.swing.JLabel();
        lbErrorDuration = new javax.swing.JLabel();
        lbErrorGenre = new javax.swing.JLabel();
        lbErrorReleaseYear = new javax.swing.JLabel();
        lbErrorAddedAt = new javax.swing.JLabel();
        spActorsForMovie = new javax.swing.JScrollPane();
        lsActors = new javax.swing.JList<>();
        spAllActors = new javax.swing.JScrollPane();
        lsAllActors = new javax.swing.JList<>();
        lbActors = new javax.swing.JLabel();
        lbAllActors = new javax.swing.JLabel();
        spDirectors = new javax.swing.JScrollPane();
        lsDirectors = new javax.swing.JList<>();
        lbDirectors = new javax.swing.JLabel();
        spAllDirectors = new javax.swing.JScrollPane();
        lsAllDirectors = new javax.swing.JList<>();
        lbAllDirectors = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        taDescription = new javax.swing.JTextArea();
        lbPosterIcon = new javax.swing.JLabel();
        btnAddMovie = new javax.swing.JButton();
        btnUpdateMovie = new javax.swing.JButton();
        btnDeleteMovie = new javax.swing.JButton();
        btnChooseImage = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        taPicturePath = new javax.swing.JTextArea();
        btnRemoveActor = new javax.swing.JButton();
        btnRemoveDirector = new javax.swing.JButton();
        btnSaveMoviesToXML = new javax.swing.JButton();
        lbErrorPoster = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(1200, 600));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        tbMovieModelTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbMovieModelTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbMovieModelTableMouseClicked(evt);
            }
        });
        tbMovieModelTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbMovieModelTableKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tbMovieModelTable);

        lbTitle.setText("Title");

        lbDescription.setText("Description");

        lbDuration.setText("Duration");

        lbGenre.setText("Genre");

        lbReleaseYear.setText("Release year");

        lbAddedAt.setText("Added at");

        lbErrorTitle.setForeground(new java.awt.Color(204, 0, 0));
        lbErrorTitle.setText("X");

        lbErrorDescription.setForeground(new java.awt.Color(204, 0, 0));
        lbErrorDescription.setText("X");

        lbErrorDuration.setForeground(new java.awt.Color(204, 0, 0));
        lbErrorDuration.setText("X");

        lbErrorGenre.setForeground(new java.awt.Color(204, 0, 0));
        lbErrorGenre.setText("X");

        lbErrorReleaseYear.setForeground(new java.awt.Color(204, 0, 0));
        lbErrorReleaseYear.setText("X");

        lbErrorAddedAt.setForeground(new java.awt.Color(204, 0, 0));
        lbErrorAddedAt.setText("X");

        spActorsForMovie.setViewportView(lsActors);

        spAllActors.setViewportView(lsAllActors);

        lbActors.setText("Actors");

        lbAllActors.setText("All Actors");

        spDirectors.setViewportView(lsDirectors);

        lbDirectors.setText("Directors");

        spAllDirectors.setViewportView(lsAllDirectors);

        lbAllDirectors.setText("All Directors");

        taDescription.setColumns(20);
        taDescription.setRows(5);
        jScrollPane2.setViewportView(taDescription);

        lbPosterIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/no_image.png"))); // NOI18N

        btnAddMovie.setText("Add Movie");
        btnAddMovie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddMovieActionPerformed(evt);
            }
        });

        btnUpdateMovie.setText("Update Movie");
        btnUpdateMovie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateMovieActionPerformed(evt);
            }
        });

        btnDeleteMovie.setText("Delete Movie");
        btnDeleteMovie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteMovieActionPerformed(evt);
            }
        });

        btnChooseImage.setText("Choose Poster");
        btnChooseImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChooseImageActionPerformed(evt);
            }
        });

        taPicturePath.setColumns(20);
        taPicturePath.setRows(5);
        jScrollPane3.setViewportView(taPicturePath);

        btnRemoveActor.setText("Remove actor");
        btnRemoveActor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActorActionPerformed(evt);
            }
        });

        btnRemoveDirector.setText("Remove director");
        btnRemoveDirector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveDirectorActionPerformed(evt);
            }
        });

        btnSaveMoviesToXML.setText("Save Movies To XML");
        btnSaveMoviesToXML.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveMoviesToXMLActionPerformed(evt);
            }
        });

        lbErrorPoster.setForeground(new java.awt.Color(204, 0, 0));
        lbErrorPoster.setText("X");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(spActorsForMovie, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                                    .addComponent(btnRemoveActor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(spAllActors, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(spDirectors, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnRemoveDirector, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(spAllDirectors, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(90, 90, 90)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnDeleteMovie, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnAddMovie, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnUpdateMovie, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnSaveMoviesToXML, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane3)
                                    .addComponent(btnChooseImage, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbErrorPoster, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(46, 46, 46)
                                .addComponent(lbActors, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(113, 113, 113)
                                .addComponent(lbAllActors, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(94, 94, 94)
                                .addComponent(lbDirectors)
                                .addGap(109, 109, 109)
                                .addComponent(lbAllDirectors)))
                        .addGap(21, 21, 21))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(lbAddedAt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lbReleaseYear, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                                    .addComponent(lbGenre, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lbDuration, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lbDescription, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lbTitle, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(tfTitle)
                                    .addComponent(tfDuration, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                                    .addComponent(tfGenre, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                                    .addComponent(tfReleaseYear, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                                    .addComponent(tfAddedAt)
                                    .addComponent(jScrollPane2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lbErrorTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                                    .addComponent(lbErrorDescription, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lbErrorDuration, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lbErrorGenre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lbErrorReleaseYear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lbErrorAddedAt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, Short.MAX_VALUE)
                                .addComponent(lbPosterIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 521, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lbTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(tfTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                                    .addComponent(lbErrorTitle))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(34, 34, 34)
                                        .addComponent(lbErrorDescription)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(tfDuration, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbErrorDuration)
                                    .addComponent(lbDuration, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(tfGenre, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbErrorGenre))
                            .addComponent(lbGenre, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbReleaseYear, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(tfReleaseYear)
                                .addComponent(lbErrorReleaseYear)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbAddedAt, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tfAddedAt, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbErrorAddedAt)))
                    .addComponent(lbPosterIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbActors)
                            .addComponent(lbAllActors)
                            .addComponent(lbDirectors)
                            .addComponent(lbAllDirectors))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnChooseImage, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lbErrorPoster))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(spDirectors, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btnRemoveDirector))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(39, 39, 39)
                                            .addComponent(btnUpdateMovie, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(btnDeleteMovie, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(btnSaveMoviesToXML, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(spAllDirectors, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
                                        .addComponent(spAllActors, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                            .addComponent(spActorsForMovie, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(btnRemoveActor))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(btnAddMovie, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(72, 72, 72))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnDeleteMovieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteMovieActionPerformed
        if (selectedModel == null) {
            MessageUtils.showInformationMessage("Wrong operation", "Please choose movie to delete!");
            return;
        }
        if (MessageUtils.showConfirmDialog(
                "Delete movie",
                "Do you really want to delete the movie?")) {
            try {
                userController.DeleteMovieDB(selectedModel.getMovieId());
                moviesTableModel.setMovies(userController.getMoviesForModel());

                clearForm();
                initModel();
            } catch (Exception ex) {
                Logger.getLogger(ManageMoviesView.class.getName()).log(Level.SEVERE, null, ex);
                MessageUtils.showErrorMessage("Error", "Unable to delete movie!");
            }
        }
    }//GEN-LAST:event_btnDeleteMovieActionPerformed

    private void btnUpdateMovieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateMovieActionPerformed

        if (selectedModel == null) {
            MessageUtils.showInformationMessage("Wrong operation", "Please choose movie to update");
            return;
        }

        try {
            if (!taPicturePath.getText().trim().equals(selectedModel.getPoster())) {
                if (selectedModel.getPoster() != null) {
                    Files.deleteIfExists(Paths.get(selectedModel.getPoster()));
                }
                String localPicturePath = uploadPicture();
                selectedModel.setPoster(localPicturePath);
            }

            selectedModel.setTitle(tfTitle.getText().trim());
            selectedModel.setDescription(taDescription.getText().trim());

            int releaseYear = Integer.parseInt(tfReleaseYear.getText().trim());
            selectedModel.setReleaseYear(releaseYear);

            int duration = Integer.parseInt(tfDuration.getText().trim());
            selectedModel.setDuration(duration);

            selectedModel.setGenre(tfGenre.getText().trim());

            selectedModel.setAddedAt(LocalDateTime.parse(tfAddedAt.getText().trim(), MovieModel.DATE_FORMATTER));

            if (!formValid()) {
                return;
            }
            
            if (checkIfMovieExists(selectedModel.getTitle())) {
                //MessageUtils.showErrorMessage("Error", "Movie  with this title already exists,please choose another name!");
                lbErrorTitle.setText("Movie with this title aleady exists!");
                lbErrorTitle.show();
                return;
            }

            userController.updateMovieDB(selectedModel);
            moviesTableModel.setMovies(userController.getMoviesForModel());

            clearForm();
            executeAllCommands();
            initModel();
        } catch (DateTimeParseException dtex) {
            MessageUtils.showErrorMessage("Date Error", "Invalid date-time format!");
        } catch (NumberFormatException numberFormatException) {
            MessageUtils.showErrorMessage("Number Error", "Unable to update movie, year or duration format error!");
        } catch (Exception ex) {
            Logger.getLogger(ManageMoviesView.class.getName()).log(Level.SEVERE, null, ex);
            MessageUtils.showErrorMessage("Error", "Unable to update movie!");
        }
    }//GEN-LAST:event_btnUpdateMovieActionPerformed

    private void btnAddMovieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddMovieActionPerformed
        try {

            if (!formValid()) {
                return;
            }

            if (checkIfMovieExists(selectedModel.getTitle())) {
                //MessageUtils.showErrorMessage("Error", "Movie  with this title already exists,please choose another name!");
                lbErrorTitle.setText("Movie with this title aleady exists!");
                lbErrorTitle.show();
                return;
            }

            undoAllCommands();
            userController.createMovieDB(selectedModel);
            moviesTableModel.setMovies(userController.getMoviesForModel());

            clearForm();
        } catch (Exception ex) {
            Logger.getLogger(ManageUsersView.class.getName()).log(Level.SEVERE, null, ex);
            MessageUtils.showErrorMessage("Error", "Unable to create movie!");
        }
    }//GEN-LAST:event_btnAddMovieActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        init();
        loadActorsAndDirectors();
    }//GEN-LAST:event_formComponentShown

    private void btnChooseImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChooseImageActionPerformed
        Optional<File> fileOptional = FileUtils.uploadFile("Images", "jpg", "jpeg", "png");
        File file = fileOptional.get();
        if (file == null) {
            return;
        }
        taPicturePath.setText(file.getAbsolutePath());
        setIcon(lbPosterIcon, file);
    }//GEN-LAST:event_btnChooseImageActionPerformed

    private void tbMovieModelTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbMovieModelTableMouseClicked
        showMovie();
    }//GEN-LAST:event_tbMovieModelTableMouseClicked

    private void btnRemoveActorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActorActionPerformed
        ActorModel selectedActor = lsActors.getSelectedValue();
        if (selectedActor != null) {
            try {
                RemoveActorCommand removeCommand = new RemoveActorCommand(actors, selectedActor, selectedModel);
                _commandInvoker.execute(removeCommand);
                loadActorsModel();
            } catch (SQLException ex) {
                Logger.getLogger(ManageMoviesView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnRemoveActorActionPerformed

    private void btnRemoveDirectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveDirectorActionPerformed
        DirectorModel selectedDirector = lsDirectors.getSelectedValue();
        if (selectedDirector != null) {
            try {
                RemoveDirectorCommand removeCommand = new RemoveDirectorCommand(directors, selectedDirector, selectedModel);
                _commandInvoker.execute(removeCommand);
                loadDirectorsModel();
            } catch (SQLException ex) {
                Logger.getLogger(ManageMoviesView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnRemoveDirectorActionPerformed

    private void btnSaveMoviesToXMLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveMoviesToXMLActionPerformed
        try {

            ifFileExists();

            List<MovieJB> moviesJB = convertMovieToJAXB(userController.getMoviesForModel());

            Channel channel = new Channel();
            channel.setMovies(moviesJB);

            MovieArchiveJB movieArchive = new MovieArchiveJB();
            movieArchive.setChannel(channel);

            JAXBUtils.save(movieArchive, FILENAME);
            MessageUtils.showInformationMessage("Info", "Movies saved");
        } catch (Exception ex) {
            MessageUtils.showErrorMessage("Error", "Unable to save movies!");
            Logger.getLogger(ManageMoviesView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnSaveMoviesToXMLActionPerformed

    private void tbMovieModelTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbMovieModelTableKeyReleased
        showMovie();
    }//GEN-LAST:event_tbMovieModelTableKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddMovie;
    private javax.swing.JButton btnChooseImage;
    private javax.swing.JButton btnDeleteMovie;
    private javax.swing.JButton btnRemoveActor;
    private javax.swing.JButton btnRemoveDirector;
    private javax.swing.JButton btnSaveMoviesToXML;
    private javax.swing.JButton btnUpdateMovie;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lbActors;
    private javax.swing.JLabel lbAddedAt;
    private javax.swing.JLabel lbAllActors;
    private javax.swing.JLabel lbAllDirectors;
    private javax.swing.JLabel lbDescription;
    private javax.swing.JLabel lbDirectors;
    private javax.swing.JLabel lbDuration;
    private javax.swing.JLabel lbErrorAddedAt;
    private javax.swing.JLabel lbErrorDescription;
    private javax.swing.JLabel lbErrorDuration;
    private javax.swing.JLabel lbErrorGenre;
    private javax.swing.JLabel lbErrorPoster;
    private javax.swing.JLabel lbErrorReleaseYear;
    private javax.swing.JLabel lbErrorTitle;
    private javax.swing.JLabel lbGenre;
    private javax.swing.JLabel lbPosterIcon;
    private javax.swing.JLabel lbReleaseYear;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JList<ActorModel> lsActors;
    private javax.swing.JList<ActorModel> lsAllActors;
    private javax.swing.JList<DirectorModel> lsAllDirectors;
    private javax.swing.JList<DirectorModel> lsDirectors;
    private javax.swing.JScrollPane spActorsForMovie;
    private javax.swing.JScrollPane spAllActors;
    private javax.swing.JScrollPane spAllDirectors;
    private javax.swing.JScrollPane spDirectors;
    private javax.swing.JTextArea taDescription;
    private javax.swing.JTextArea taPicturePath;
    private javax.swing.JTable tbMovieModelTable;
    private javax.swing.JTextField tfAddedAt;
    private javax.swing.JTextField tfDuration;
    private javax.swing.JTextField tfGenre;
    private javax.swing.JTextField tfReleaseYear;
    private javax.swing.JTextField tfTitle;
    // End of variables declaration//GEN-END:variables
}
