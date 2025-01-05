# 3D Graphics Rendering Engine From Scratch

## Description

This project implements a **3D rendering engine** from scratch using **Java**. The engine can load **3D object files** (e.g., .obj files created using tools like Blender) along with **optional texture image files** to render them on the screen. The program allows you to navigate and interact with the 3D environment, providing the ability to view objects from any angle and distance.

The objects are rendered using **triangles** for full compatibility with Blender and other 3D modeling software. The project also features rudimentary **texture mipmapping**, **culling**, and **clipping** for improved performance and realism.

### Features:
- **Full projection matrix**: Includes Field of View (FOV), perspective projection, near and far planes, etc.
- **Directional lighting** to simulate light sources in the 3D world.
- **Textures**: Support for object textures (optional but enhances appearance).
- **Texture mipmapping**: Improves performance by using lower-resolution textures at distant objects.
- **Culling and clipping**: Removes objects outside the view frustum to optimize rendering.
- **Import `.obj` files**: Supports importing 3D objects created with tools like Blender (objects must be composed of triangles).
- **Movement and camera control**: Navigate and look around in the 3D environment.

## DESMOS DEMO

The initial prototype of this project began on **Desmos**, where I experimented with projecting 3D points onto a 2D screen. The graphing calculator provided an easy environment to play around with 3D concepts. You can view my **Desmos prototype** [here](https://www.desmos.com/calculator/v5mwlcvbec).

## Technologies Used:
- **Java**: The main programming language for the rendering engine.
- **.obj files**: For importing 3D object data.
- **Textures**: Optional image files for decorating 3D objects.

## How to Run

To run the project, follow these steps:

1. Clone the repository:
    ```bash
    git clone https://github.com/YourUsername/3d-rendering-engine.git
    cd 3d-rendering-engine
    ```

2. Build and run the project:
    ```bash
    javac Main.java
    java Main
    ```

3. Interact with the 3D scene in the program:
    - Use mouse and keyboard controls to move around the 3D space and view objects from different angles.

## Project Structure

- `/`: Contains the Java source code for the engine.
- `/objects/`: Folder for 3D models (`.obj` files)
- `/textures/`: Folder for textures (image files).
- `README.md`: Project description and instructions.
- `Main.java`: Entry point of the program for running the engine.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

